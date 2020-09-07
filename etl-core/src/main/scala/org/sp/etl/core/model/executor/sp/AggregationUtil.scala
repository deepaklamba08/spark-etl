package org.sp.etl.core.model.executor.sp

import org.apache.spark.sql.{Column, DataFrame, functions}
import org.sp.etl.core.model.DataBag
import org.sp.etl.function.column.agg.{AggregateValue, GroupByDatasetFunction, MaxValue}

import scala.collection.JavaConverters._

object AggregationUtil {


  def aggregateDatabag(dataBag: DataBag, grFunction: GroupByDatasetFunction): DataFrame = {
    val kvDataset = dataBag.dataset.groupBy(grFunction.getGroupByColumns.asScala.map(functions.col): _*)
    val aggregateColumns = grFunction.getAggregateValues.asScala.map(this.aggregateColumn)
    kvDataset.agg(aggregateColumns.head, aggregateColumns.tail: _*)
  }

  def aggregateColumn(aggValue: AggregateValue): Column =
    aggValue match {
      case max: MaxValue => functions.max(functions.col(max.getColumnName)).alias(max.getResultAlias)
      case other => throw new UnsupportedOperationException(s"aggregate function - ${other} not supported")
    }
}
