package org.sp.etl.core.model.executor

import org.sp.etl.common.model.job.Job
import org.sp.etl.core.metrics.JobMetrics
import org.sp.etl.core.model.{DataBag, Status, SuccessStatus}

trait JobExecutor {

  def executeJob(job: Job): JobExecutionResult
}


case class JobExecutionResult(dataBag: DataBag, jobMetrics: JobMetrics, executionMessage: String = null, status: Status = SuccessStatus)