package org.sp.etl.core.model.executor

import org.sp.etl.common.model.step.Step
import org.sp.etl.core.metrics.StepMetrics
import org.sp.etl.core.model.{DataBag, Databags, Status, SuccessStatus}

trait StepExecutor {

  def executeStep(step: Step, otherDatabags: Databags): StepExecutionResult
}

case class StepExecutionResult(primaryDatabag: DataBag, otherDatabags: Databags, stepMetrics: StepMetrics, executionMessage: String = null, status: Status = SuccessStatus)