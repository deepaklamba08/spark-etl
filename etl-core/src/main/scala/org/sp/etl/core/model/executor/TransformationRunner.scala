package org.sp.etl.core.model.executor

import org.sp.etl.core.metrics.FunctionMetrics
import org.sp.etl.core.model.{DataBag, Transformation}

trait TransformationRunner {

  def runTransformation(transformation: Transformation): TransformationResult
}

case class TransformationResult(dataBag: DataBag, functionMetrics: Seq[FunctionMetrics])