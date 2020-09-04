package org.sp.etl.core.model.executor.sp

import java.util.Date

import org.apache.spark.sql.functions
import org.sp.etl.core.metrics.FunctionMetrics
import org.sp.etl.core.model.{DataBag, Databags, Transformation}
import org.sp.etl.core.model.executor.{TransformationResult, TransformationRunner}
import org.sp.etl.function.DatasetFunction
import org.sp.etl.function.column.{AddConstantValueFunction, ColumnFunction, DropColumnColumnFunction, RenameColumnFunction}
import org.sp.etl.function.dataset.InnerJoinDatasetFunction

import scala.collection.mutable.ListBuffer

class SparkTransformationRunner extends TransformationRunner {

  override def runTransformation(transformation: Transformation): TransformationResult = {
    val metrics = ListBuffer[FunctionMetrics]()
    val rDatabag = transformation.functions.foldLeft(transformation.primary)((databag, trFunction) => {
      val functionMetrics = new FunctionMetrics.FunctionMetricsBuilder(trFunction.name(), new Date())
      val iDatabag = trFunction match {
        case cFx: ColumnFunction => this.runColumnFunction(cFx, databag)
        case dFx: DatasetFunction => this.runDatasetFunction(dFx, databag, transformation.secondary)
      }
      metrics.+=(functionMetrics.withEndTime(new Date()).build())
      iDatabag
    })

    TransformationResult(rDatabag, metrics)
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
