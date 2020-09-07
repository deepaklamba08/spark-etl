package org.sp.etl.core.model.executor.sp

import java.util.Date

import org.slf4j.LoggerFactory
import org.sp.etl.common.exception.EtlExceptions.InvalidConfigurationException
import org.sp.etl.common.model.job.Job
import org.sp.etl.common.util.JsonDataObject
import org.sp.etl.core.metrics.JobMetrics.JobMetricsBuilder
import org.sp.etl.core.model.{Databags, FailedStatus, SuccessStatus}
import org.sp.etl.core.model.executor.sp.data.loader.SparkDataLoader
import org.sp.etl.core.model.executor.{JobExecutionResult, JobExecutor}
import org.sp.etl.common.model.step.Step

import scala.collection.JavaConverters._

class SparkJobExecutor(appName: String, sparkConfig: JsonDataObject) extends JobExecutor {

  private val logger = LoggerFactory.getLogger(this.getClass)

  override def executeJob(job: Job): JobExecutionResult = {
    logger.debug("executing - SparkJobExecutor.executeJob()")
    if (!this.validateJob(job)) {
      throw new InvalidConfigurationException("multiple steps can not have same output source name or same step name")
    }

    val sparkSession = SparkHelper.createSparkSession(appName, sparkConfig)
    val steps = job.getSteps.asScala.sortWith((s1, s2) => s1.getStepIndex < s2.getStepIndex)
    logger.debug(s"number of steps - ${steps.size}")

    val stepExecutor = new SparkStepExecutor(new SparkTransformationRunner(), new SparkDataLoader(sparkSession))

    var jobMetricsBuilder = new JobMetricsBuilder(job.getJobName, new Date())

    val finalResult = steps.tail.foldLeft(stepExecutor.executeStep(steps.head, Databags.emptyDatabag))((stepResult, aStep) => {
      stepResult.status match {
        case SuccessStatus =>
          jobMetricsBuilder = jobMetricsBuilder.withStepMetrics(stepResult.stepMetrics)
          stepExecutor.executeStep(aStep, Databags(stepResult.otherDatabags.getDatabags))
        case FailedStatus => stepResult
      }
    })

    JobExecutionResult(finalResult.primaryDatabag, jobMetricsBuilder.withEndTime(new Date()).build(), finalResult.executionMessage, finalResult.status)
  }

  private def validateJob(job: Job) = {
    this.groupSteps(job, s => s.getStepName).fold(
      this.groupSteps(job, s => s.getOutputSourceName).fold(true)
      (_ => false))(_ => false)
  }

  private def groupSteps(job: Job, fx: Step => String) = {
    job.getSteps.asScala.map(fx).groupBy(c => c).find(_._2.size > 1).map(_._2.size)
  }

}
