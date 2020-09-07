package org.sp.etl.core.model.executor.sp

import java.util

import org.apache.spark.sql.{DataFrame, functions}
import org.apache.spark.storage.StorageLevel
import org.sp.etl.common.exception.EtlExceptions.InvalidConfigurationException
import org.sp.etl.core.model.executor.{FunctionExecutionResult, TransformationRunner}
import org.sp.etl.core.model.{DataBag, Databags, FailedStatus}
import org.sp.etl.function.column.DateAndTimeFunction.{CurrentDateFunction, CurrentTimestampFunction, ToDateFunction, ToTimestampFunction}
import org.sp.etl.function.column.SortDatasetFunction.SortOrder
import org.sp.etl.function.column.agg.GroupByDatasetFunction
import org.sp.etl.function.column.math.SumColumnFunction
import org.sp.etl.function.column.{AddConstantValueFunction, ColumnFunction, DropColumnFunction, FilterDatasetFunction, PersistDatasetFunction, RenameColumnFunction, RepartitionDatasetFunction, SortDatasetFunction, UnPersistDatasetFunction}
import org.sp.etl.function.dataset.{DatasetRegisterAsTableFunction, DatasetUnionFunction, InnerJoinDatasetFunction, LeftJoinDatasetFunction, RightJoinDatasetFunction}
import org.sp.etl.function.{DatasetFunction, EtlFunction}

import scala.util.{Failure, Success, Try}
import scala.collection.JavaConverters._

class SparkTransformationRunner extends TransformationRunner {

  override def executeFunction(etlFunction: EtlFunction, primary: DataBag, secondary: Databags): FunctionExecutionResult = {
    Try(runFunction(etlFunction, primary, secondary)) match {
      case Success(databag) => FunctionExecutionResult(databag)
      case Failure(cause) => FunctionExecutionResult(null, cause.getMessage, FailedStatus)
    }
  }

  private def runFunction(etlFunction: EtlFunction, primary: DataBag, secondary: Databags) = {
    etlFunction match {
      case cFx: ColumnFunction => this.runColumnFunction(cFx, primary)
      case dFx: DatasetFunction => this.runDatasetFunction(dFx, primary, secondary)
    }
  }

  private def runColumnFunction(cFx: ColumnFunction, dataBag: DataBag) = {
    val opDataset = cFx match {
      case rn: RenameColumnFunction => dataBag.dataset.withColumnRenamed(rn.getOldName, rn.getNewName)
      case ac: AddConstantValueFunction => dataBag.dataset.withColumn(ac.getColumnName, functions.lit(ac.getValue))
      case dc: DropColumnFunction => dataBag.dataset.drop(dc.getColumnName)
      case rp: RepartitionDatasetFunction => dataBag.dataset.repartition(rp.getNumPartitons, rp.getPartitionColumns.asScala.map(functions.col): _*)
      case pr: PersistDatasetFunction => dataBag.dataset.persist(this.getStorageLevel(pr.getPersistLevel))
      case _: UnPersistDatasetFunction => dataBag.dataset.unpersist()
      case gr: GroupByDatasetFunction => AggregationUtil.aggregateDatabag(dataBag, gr)
      case flt: FilterDatasetFunction => dataBag.dataset.filter(flt.getFilterCondition)
      case so: SortDatasetFunction => this.sortDataset(dataBag.dataset, so.getSortColumns)
      case cd: CurrentDateFunction => dataBag.dataset.withColumn(cd.getColumnName, functions.current_date())
      case ct: CurrentTimestampFunction => dataBag.dataset.withColumn(ct.getColumnName, functions.current_timestamp())
      case td: ToDateFunction => dataBag.dataset.withColumn(td.getColumnName, functions.to_date(functions.col(td.getSourceColumn), td.getFormat))
      case tt: ToTimestampFunction => dataBag.dataset.withColumn(tt.getColumnName, functions.to_timestamp(functions.col(tt.getSourceColumn), tt.getFormat))
      case sum: SumColumnFunction => dataBag.dataset.withColumn(sum.getResultColumnName, sum.getColumns.asScala.tail.foldLeft(functions.col(sum.getColumns.asScala.head))((a, b) => a + b))
      case other => throw new UnsupportedOperationException(s"unsupported dataset function - ${other}")
    }
    DataBag(dataBag.name, dataBag.alias, opDataset)
  }

  private def runDatasetFunction(dFx: DatasetFunction, pDataBag: DataBag, databags: Databags) = {
    val opDataset = dFx match {
      case ij: InnerJoinDatasetFunction =>
        val rightDs = databags.getDatabag(ij.getRightDatasetName).dataset
        this.joinDatasets(pDataBag.dataset, ij.getLeftDatasetColumn, rightDs, ij.getRightDatasetColumn, ij.joinType())
      case lj: LeftJoinDatasetFunction =>
        val rightDs = databags.getDatabag(lj.getRightDatasetName).dataset
        this.joinDatasets(pDataBag.dataset, lj.getLeftDatasetColumn, rightDs, lj.getRightDatasetColumn, lj.joinType())
      case rj: RightJoinDatasetFunction =>
        val rightDs = databags.getDatabag(rj.getRightDatasetName).dataset
        this.joinDatasets(pDataBag.dataset, rj.getLeftDatasetColumn, rightDs, rj.getRightDatasetColumn, rj.joinType())
      case un: DatasetUnionFunction =>
        val sDatabag = databags.getDatabag(un.getSecondDatasetName).dataset
        if (un.isUnionByName) {
          pDataBag.dataset.unionByName(sDatabag)
        } else {
          pDataBag.dataset.union(sDatabag)
        }
      case rd: DatasetRegisterAsTableFunction => pDataBag.dataset.createTempView(rd.getDatasetName)
        pDataBag.dataset
      case other => throw new UnsupportedOperationException(s"unsupported dataset function - ${other}")
    }
    DataBag(pDataBag.name, pDataBag.alias, opDataset)
  }

  private def getStorageLevel(storaeLevel: String) = {
    storaeLevel.toUpperCase match {
      case "DISK_ONLY" => StorageLevel.DISK_ONLY
      case "DISK_ONLY_2" => StorageLevel.DISK_ONLY_2
      case "MEMORY_ONLY" => StorageLevel.MEMORY_ONLY
      case "MEMORY_ONLY_2" => StorageLevel.MEMORY_ONLY_2
      case "MEMORY_ONLY_SER" => StorageLevel.MEMORY_ONLY_SER
      case "MEMORY_ONLY_SER_2" => StorageLevel.MEMORY_ONLY_SER_2
      case "MEMORY_AND_DISK" => StorageLevel.MEMORY_AND_DISK
      case "MEMORY_AND_DISK_2" => StorageLevel.MEMORY_AND_DISK_2
      case "MEMORY_AND_DISK_SER" => StorageLevel.MEMORY_AND_DISK_SER
      case "MEMORY_AND_DISK_SER_2" => StorageLevel.MEMORY_AND_DISK_SER_2
      case other => throw new InvalidConfigurationException(s"invalid storage level -$other")
    }
  }

  def sortDataset(dataset: DataFrame, sortColumns: util.Map[String, SortDatasetFunction.SortOrder]): DataFrame = {
    val sortSequence = sortColumns.asScala.map(p => p._2 match {
      case SortOrder.asc => functions.col(p._1).asc.alias(p._1)
      case SortOrder.asc_null_first => functions.col(p._1).asc_nulls_first.alias(p._1)
      case SortOrder.asc_null_last => functions.col(p._1).asc_nulls_last.alias(p._1)
      case SortOrder.desc => functions.col(p._1).desc.alias(p._1)
      case SortOrder.desc_null_first => functions.col(p._1).desc_nulls_first.alias(p._1)
      case SortOrder.desc_null_last => functions.col(p._1).desc_nulls_last.alias(p._1)
    }).toSeq
    dataset.sort(sortSequence: _*)
  }

  private def joinDatasets(leftDataset: DataFrame, leftDatasetColumn: String, rightDataset: DataFrame, rightDatasetColumn: String, joinType: String) = {
    val jc = leftDataset(leftDatasetColumn) === rightDataset(rightDatasetColumn)
    leftDataset.join(rightDataset, jc, joinType)
  }
}
