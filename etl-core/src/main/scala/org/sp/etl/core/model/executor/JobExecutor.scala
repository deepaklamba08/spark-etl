package org.sp.etl.core.model.executor

import org.sp.etl.core.model.DataBag
import org.sp.etl.common.model.job.Job
import org.sp.etl.core.metrics.JobMetrics

trait JobExecutor {

  def executeJob(job: Job): JobExecutionResult
}


case class JobExecutionResult(dataBag: DataBag, jobMetrics: JobMetrics)