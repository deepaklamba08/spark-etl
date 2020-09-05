package org.sp.etl.core.model.executor.sp

import org.apache.spark.sql.functions
import org.sp.etl.core.model.executor.{FunctionExecutionResult, TransformationRunner}
import org.sp.etl.core.model.{DataBag, Databags, FailedStatus}
import org.sp.etl.function.column.{AddConstantValueFunction, ColumnFunction, DropColumnColumnFunction, RenameColumnFunction}
import org.sp.etl.function.dataset.InnerJoinDatasetFunction
import org.sp.etl.function.{DatasetFunction, EtlFunction}

import scala.util.{Failure, Success, Try}

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
      case dc: DropColumnColumnFunction => dataBag.dataset.drop(dc.getColumnName)
      case other => throw new UnsupportedOperationException(s"unsupported dataset function - ${other}")
    }
    DataBag(dataBag.name, dataBag.alias, opDataset)
  }

  private def runDatasetFunction(dFx: DatasetFunction, pDataBag: DataBag, databags: Databags) = {
    val opDataset = dFx match {
      case ij: InnerJoinDatasetFunction =>
        val rightDs = databags.getDatabag(ij.getRightDatasetName).dataset
        val jc = pDataBag.dataset(ij.getLeftDatasetColumn) === rightDs(ij.getRightDatasetColumn)
        pDataBag.dataset.join(rightDs, jc, ij.joinType())
      case other => throw new UnsupportedOperationException(s"unsupported dataset function - ${other}")
    }
    DataBag(pDataBag.name, pDataBag.alias, opDataset)
  }

}
