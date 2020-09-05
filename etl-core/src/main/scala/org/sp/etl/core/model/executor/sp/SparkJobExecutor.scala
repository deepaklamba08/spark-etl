package org.sp.etl.core.model.executor.sp

import java.util.Date

import org.slf4j.LoggerFactory
import org.sp.etl.common.model.job.Job
import org.sp.etl.common.util.JsonDataObject
import org.sp.etl.core.metrics.JobMetrics.JobMetricsBuilder
import org.sp.etl.core.model.{Databags, FailedStatus, SuccessStatus}
import org.sp.etl.core.model.executor.sp.data.loader.SparkDataLoader
import org.sp.etl.core.model.executor.{JobExecutionResult, JobExecutor}

import scala.collection.JavaConverters._

class SparkJobExecutor(appName: String, sparkConfig: JsonDataObject) extends JobExecutor {

  private val logger = LoggerFactory.getLogger(this.getClass)

  override def executeJob(job: Job): JobExecutionResult = {
    logger.debug("executing - SparkJobExecutor.executeJob()")
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


}
