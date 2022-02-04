package org.sp.etl.core.model.executor.sp.function

import java.util
import org.apache.spark.sql.{DataFrame, functions}
import org.apache.spark.storage.StorageLevel
import org.sp.etl.common.exception.EtlExceptions.InvalidConfigurationException
import org.sp.etl.core.model.DataBag
import org.sp.etl.core.model.executor.sp.AggregationUtil
import org.sp.etl.function.column.DateAndTimeFunction._
import org.sp.etl.function.column.SortDatasetFunction.SortOrder
import org.sp.etl.function.column.{AddConstantValueFunction, CastColumnFunction, ColumnFunction, DropColumnFunction, FilterDatasetFunction, PersistDatasetFunction, RenameColumnFunction, RepartitionDatasetFunction, SortDatasetFunction}
import org.sp.etl.function.column.agg.GroupByDatasetFunction
import org.sp.etl.function.column.math.SumColumnFunction
import org.sp.etl.function.dataset.UnPersistDatasetFunction

import scala.collection.JavaConverters._

abstract class ColumnFunctionRunner(function: ColumnFunction) extends FunctionRunner[ColumnFunction](function) {
  def run(dataBag: DataBag): DataBag
}

object ColumnFunctionRunnerFactory {

  def getFunctionRunner(function: ColumnFunction): ColumnFunctionRunner = {

    function match {
      case rn: RenameColumnFunction => new RenameColumnColumnFunctionRunner(rn)
      case ac: AddConstantValueFunction => new AddConstantValueColumnFunctionRunner(ac)
      case dc: DropColumnFunction => new DropColumnColumnFunctionRunner(dc)
      case rp: RepartitionDatasetFunction => new RepartitionDatasetColumnFunctionRunner(rp)
      case pr: PersistDatasetFunction => new PersistDatasetColumnFunctionRunner(pr)
      case un: UnPersistDatasetFunction => new UnPersistDatasetColumnFunctionRunner(un)
      case gr: GroupByDatasetFunction => new GroupByDatasetColumnFunctionRunner(gr)
      case flt: FilterDatasetFunction => new FilterDatasetColumnFunctionRunner(flt)
      case so: SortDatasetFunction => new SortDatasetColumnFunctionRunner(so)
      case cd: CurrentDateFunction => new CurrentDateColumnFunctionRunner(cd)
      case ct: CurrentTimestampFunction => new CurrentTimestampColumnFunctionRunner(ct)
      case td: ToDateFunction => new ToDateColumnFunctionRunner(td)
      case tt: ToTimestampFunction => new ToTimestampColumnFunctionRunner(tt)
      case sum: SumColumnFunction => new SumColumnColumnFunctionRunner(sum)
      case cast: CastColumnFunction => new CastColumnColumnFunctionRunner(cast)
      case other => throw new UnsupportedOperationException(s"unsupported dataset function - ${other}")
    }
  }

}


class RenameColumnColumnFunctionRunner(function: RenameColumnFunction) extends ColumnFunctionRunner(function) {
  override def run(dataBag: DataBag): DataBag = DataBag(dataBag.name, dataBag.alias, dataBag.dataset.withColumnRenamed(function.getOldName, function.getNewName))
}

class AddConstantValueColumnFunctionRunner(function: AddConstantValueFunction) extends ColumnFunctionRunner(function) {
  override def run(dataBag: DataBag): DataBag = DataBag(dataBag.name, dataBag.alias, dataBag.dataset.withColumn(function.getColumnName, functions.lit(function.getValue)))
}

class DropColumnColumnFunctionRunner(function: DropColumnFunction) extends ColumnFunctionRunner(function) {
  override def run(dataBag: DataBag): DataBag = DataBag(dataBag.name, dataBag.alias, dataBag.dataset.drop(function.getColumnName))
}

class RepartitionDatasetColumnFunctionRunner(function: RepartitionDatasetFunction) extends ColumnFunctionRunner(function) {
  override def run(dataBag: DataBag): DataBag = DataBag(dataBag.name, dataBag.alias, dataBag.dataset.repartition(function.getNumPartitions, function.getPartitionColumns.asScala.map(functions.col): _*))
}

class PersistDatasetColumnFunctionRunner(function: PersistDatasetFunction) extends ColumnFunctionRunner(function) {
  override def run(dataBag: DataBag): DataBag = DataBag(dataBag.name, dataBag.alias, dataBag.dataset.persist(this.getStorageLevel(function.getPersistLevel)))

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
}

class GroupByDatasetColumnFunctionRunner(function: GroupByDatasetFunction) extends ColumnFunctionRunner(function) {
  override def run(dataBag: DataBag): DataBag = DataBag(dataBag.name, dataBag.alias, AggregationUtil.aggregateDatabag(dataBag, function))
}

class FilterDatasetColumnFunctionRunner(function: FilterDatasetFunction) extends ColumnFunctionRunner(function) {
  override def run(dataBag: DataBag): DataBag = DataBag(dataBag.name, dataBag.alias, dataBag.dataset.filter(function.getFilterCondition))
}

class SortDatasetColumnFunctionRunner(function: SortDatasetFunction) extends ColumnFunctionRunner(function) {
  override def run(dataBag: DataBag): DataBag = DataBag(dataBag.name, dataBag.alias, this.sortDataset(dataBag.dataset, function.getSortColumns))

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
}

class CurrentDateColumnFunctionRunner(function: CurrentDateFunction) extends ColumnFunctionRunner(function) {
  override def run(dataBag: DataBag): DataBag = DataBag(dataBag.name, dataBag.alias, dataBag.dataset.withColumn(function.getColumnName, functions.current_date()))
}

class CurrentTimestampColumnFunctionRunner(function: CurrentTimestampFunction) extends ColumnFunctionRunner(function) {
  override def run(dataBag: DataBag): DataBag = DataBag(dataBag.name, dataBag.alias, dataBag.dataset.withColumn(function.getColumnName, functions.current_timestamp()))
}

class ToDateColumnFunctionRunner(function: ToDateFunction) extends ColumnFunctionRunner(function) {
  override def run(dataBag: DataBag): DataBag = DataBag(dataBag.name, dataBag.alias, dataBag.dataset.withColumn(function.getColumnName, functions.to_date(functions.col(function.getSourceColumn), function.getFormat)))
}

class ToTimestampColumnFunctionRunner(function: ToTimestampFunction) extends ColumnFunctionRunner(function) {
  override def run(dataBag: DataBag): DataBag = DataBag(dataBag.name, dataBag.alias, dataBag.dataset.withColumn(function.getColumnName, functions.to_timestamp(functions.col(function.getSourceColumn), function.getFormat)))
}

class SumColumnColumnFunctionRunner(function: SumColumnFunction) extends ColumnFunctionRunner(function) {
  override def run(dataBag: DataBag): DataBag = {
    val columnsToAdd = function.getColumns.asScala
    val dataset = dataBag.dataset.withColumn(function.getResultColumnName, columnsToAdd.tail.foldLeft(functions.col(columnsToAdd.head))((a, b) => a + b))
    DataBag(dataBag.name, dataBag.alias, dataset)
  }
}

class CastColumnColumnFunctionRunner(function: CastColumnFunction) extends ColumnFunctionRunner(function) {
  override def run(dataBag: DataBag): DataBag = DataBag(dataBag.name, dataBag.alias, dataBag.dataset.withColumn(function.getResultColumnName, functions.col(function.getSourceColumn).cast(function.getToType).alias(function.getResultColumnName)))
}

