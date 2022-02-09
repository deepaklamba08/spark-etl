package org.sp.etl.core.model.executor

import java.util.Date
import org.slf4j.LoggerFactory
import org.sp.etl.core.metrics.FunctionMetrics
import org.sp.etl.core.model.{SuccessStatus, _}
import org.sp.etl.function.EtlFunction

import scala.collection.mutable.ListBuffer

trait TransformationRunner {

  private val logger = LoggerFactory.getLogger(this.getClass)

  def runTransformation(transformation: Transformation): TransformationResult = {
    logger.debug("executing - TransformationRunner.runTransformation()")

    val functions = transformation.functions.iterator
    var status: Status = SuccessStatus
    var databag = transformation.primary
    var message: Option[String] = None
    val metrics = ListBuffer[FunctionMetrics]()
    while (functions.hasNext && status == SuccessStatus) {
      val trFunction = functions.next()
      val functionMetrics = new FunctionMetrics.FunctionMetricsBuilder(trFunction.getName(), new Date())
      logger.debug(s"executing function - ${trFunction.getName()}")
      val result = this.executeFunction(trFunction, databag, transformation.secondary)
      metrics.+=(functionMetrics.withEndTime(new Date()).build())
      databag = result.dataBag
      status = result.status
      message = result.executionMessage
    }
    TransformationResult(databag, metrics, status, message)

  }

  protected def executeFunction(etlFunction: EtlFunction, primary: DataBag, secondary: Databags): FunctionExecutionResult
}

case class TransformationResult(dataBag: DataBag, functionMetrics: Seq[FunctionMetrics], status: Status = SuccessStatus, executionMessage: Option[String] = None)

case class FunctionExecutionResult(dataBag: DataBag, status: Status = SuccessStatus, executionMessage: Option[String] = None)
