package org.sp.etl.core.model.executor.sp

import java.util.Date

import org.slf4j.LoggerFactory
import org.sp.etl.common.model.job.Job
import org.sp.etl.common.util.JsonDataObject
import org.sp.etl.core.metrics.JobMetrics.JobMetricsBuilder
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

    val stepExecutor = new StepExecutor(new SparkTransformationRunner(), new SparkDataLoader(sparkSession))

    var jobMetricsBuilder = new JobMetricsBuilder(job.getJobName, new Date())
    val opDatabag = steps.tail.foldLeft(stepExecutor.executeStep(steps.head, List.empty))((stepResult, aStep) => {
      jobMetricsBuilder = jobMetricsBuilder.withStepMetrics(stepResult.stepMetrics)
      stepExecutor.executeStep(aStep, stepResult.otherDatabags.getDatabags.toList)
    })

    JobExecutionResult(opDatabag.primaryDatabag, jobMetricsBuilder.withEndTime(new Date()).build())
  }


}
