package org.sp.etl.core.model.executor.sp

import java.util.Date

import org.slf4j.LoggerFactory
import org.sp.etl.common.exception.EtlExceptions.ObjectNotFoundException
import org.sp.etl.common.model.step.Step
import org.sp.etl.core.metrics.StepMetrics
import org.sp.etl.core.model._
import org.sp.etl.core.model.executor.{DataLoader, StepExecutionResult, StepExecutor}

import scala.collection.JavaConverters._

class SparkStepExecutor(dataLoader: DataLoader) extends StepExecutor {

  private val logger = LoggerFactory.getLogger(this.getClass)
  private lazy val transformationRunner = new SparkTransformationRunner()

  override def executeStep(step: Step, otherDatabags: Databags): StepExecutionResult = {
    logger.debug(s"executing step - ${step.getName}")

    val transformation = this.createTransformation(step, otherDatabags)

    val transformationResult = transformationRunner.runTransformation(transformation)
    val stepMetrics = transformationResult.functionMetrics.foldLeft(new StepMetrics.StepMetricsBuilder(step.getName, step.getStepIndex, new Date()))((acc, metrics) => acc.withFunctionMetrics(metrics))

    transformationResult.status match {
      case SuccessStatus =>
        val resultantDatabag = DataBag(step.getOutputSourceName, step.getOutputSourceAlias, transformationResult.dataBag.dataset)
        StepExecutionResult(resultantDatabag, new Databags(resultantDatabag :: otherDatabags.getDatabags), stepMetrics.withEndTime(new Date()).build())
      case FailedStatus =>
        StepExecutionResult(null, null, stepMetrics.withEndTime(new Date()).build(), transformationResult.executionMessage.get, transformationResult.status)
    }

  }

  private def createTransformation(step: Step, otherDatabags: Databags) = {
    val databags = step.getSources.asScala.map(this.lookupDatabag(dataLoader, otherDatabags) _)
    logger.debug(s"number of  datasets in step - ${databags.size}")

    val primaryInput = databags.find(_.name.equals(step.getPrimarySource)).fold(throw new ObjectNotFoundException(s"could not find primary datasets - ${step.getPrimarySource}"))(c => c)
    val secondaryInput = databags.filter(!_.name.equals(step.getPrimarySource))

    Transformation(step.getEtlFunctions.asScala.toList, primaryInput, new Databags((secondaryInput ++ otherDatabags.getDatabags).toList))
  }

  private def lookupDatabag(dataLoader: DataLoader, otherDatabags: Databags)(sourceName: String) = {
    try {
      val etlSource = EtlSourceRegistry.lookupSource(sourceName)
      dataLoader.loadData(etlSource, DataSourceRegistry.lookupDataSource(etlSource.dataSourceName()))
    }
    catch {
      case nf: ObjectNotFoundException =>
        otherDatabags.getDatabags.find(_.name.equals(sourceName)).fold(throw nf)(c => c)
      case other: Throwable => throw other
    }
  }

}

