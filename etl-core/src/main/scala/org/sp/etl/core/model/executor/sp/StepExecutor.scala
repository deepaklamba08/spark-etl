package org.sp.etl.core.model.executor.sp

import java.util.Date

import org.slf4j.LoggerFactory
import org.sp.etl.common.exception.EtlExceptions.ObjectNotFoundException
import org.sp.etl.common.model.step.Step
import org.sp.etl.core.metrics.StepMetrics
import org.sp.etl.core.model._
import org.sp.etl.core.model.executor.{DataLoader, TransformationRunner}

import scala.collection.JavaConverters._

class StepExecutor(transformationRunner: TransformationRunner, dataLoader: DataLoader) {

  private val logger = LoggerFactory.getLogger(this.getClass)

  def executeStep(step: Step, otherDatabags: List[DataBag]): StepExecutionResult = {
    logger.debug(s"executing step - ${step.getStepName}")
    val databags = step.getSources.asScala.map(s => this.lookupDatabag(s, dataLoader, otherDatabags))
    logger.debug(s"number of  datasets in step - ${databags.size}")

    val primaryInput = databags.find(_.name.equals(step.getInputSourceName)).fold(throw new ObjectNotFoundException(s"could not find primary datasets - ${step.getInputSourceName}"))(c => c)
    val secondaryInput = databags.filter(!_.name.equals(step.getInputSourceName))

    val transformation = Transformation(step.getEtlFunctions.asScala.toList, primaryInput, new Databags(secondaryInput ++ otherDatabags))

    val transformationResult = transformationRunner.runTransformation(transformation)

    val resultantDatabag = DataBag(step.getOutputSourceName, step.getOutputSourceAlias, transformationResult.dataBag.dataset)
    val stepMetrics = transformationResult.functionMetrics.foldLeft(new StepMetrics.StepMetricsBuilder(step.getStepName, step.getStepIndex, new Date()))((acc, metrics) => acc.withFunctionMetrics(metrics))

    StepExecutionResult(resultantDatabag, new Databags(resultantDatabag :: otherDatabags), stepMetrics.withEndTime(new Date()).build())
  }


  private def lookupDatabag(sourceName: String, dataLoader: DataLoader, otherDatabags: Seq[DataBag]) = {
    try {
      val etlSource = EtlSourceRegistry.lookupSource(sourceName)
      dataLoader.loadData(etlSource, DataSourceRegistry.lookupDataSource(etlSource.dataSourceName()))
    }
    catch {
      case nf: ObjectNotFoundException =>
        otherDatabags.find(_.name.equals(sourceName)).fold(throw nf)(c => c)
      case other: Throwable => throw other
    }
  }
}


case class StepExecutionResult(primaryDatabag: DataBag, otherDatabags: Databags, stepMetrics: StepMetrics)