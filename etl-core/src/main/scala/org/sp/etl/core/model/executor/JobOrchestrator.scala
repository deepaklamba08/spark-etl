package org.sp.etl.core.model.executor

import org.sp.etl.common.exception.EtlExceptions
import org.sp.etl.common.exception.EtlExceptions.ObjectNotFoundException
import org.sp.etl.common.repo.EtlRepositroty
import org.sp.etl.core.model.{DataSourceRegistry, EtlSourceRegistry, EtlTargetRegistry}
import org.sp.etl.core.util.Constants
import org.sp.etl.common.model.job.Job

import scala.collection.JavaConverters._

class JobOrchestrator(etlRepositroty: EtlRepositroty) {

  def executeJob(jobName: String) = {
    val job = this.lookupJob(jobName)
    this.registerSources(job)
    val executor = JobExecutorFactory.createJobExecutor(job.getJobName, Constants.SPARK_JOB_EXECUTOR, this.etlRepositroty.lookupObject(Constants.EXECUTOR_CONF_NAME))
    val jobExecutionResult = executor.executeJob(job)

    val etlTarget = EtlTargetRegistry.lookupTarget(job.getTargetName)
    DataConsumerFactory.createDataConsumer(Constants.SPARK_JOB_EXECUTOR).consume(jobExecutionResult.dataBag, etlTarget)
  }

  private def lookupJob(jobName: String) = {
    val etlJob = this.etlRepositroty.lookupJob(jobName)

    if (etlJob == null) {
      throw new EtlExceptions.ObjectNotFoundException(s"could not find job ${jobName} in the repository")
    } else if (!etlJob.isEnabled) {
      throw new IllegalStateException(s"job ${jobName} is disabled in the repository")
    } else {
      etlJob
    }
  }

  private def registerSources(job: Job) = {
    val sources = job.getSteps.asScala.flatMap(_.getSources.asScala).map(etlRepositroty.lookupEtlSource).filter(p => p != null)
    sources.foreach(EtlSourceRegistry.registerSource)
    sources.map(_.dataSourceName()).map(this.lookupDataSource).foreach(DataSourceRegistry.registerDataSource)

    val target = etlRepositroty.lookupEtlTarget(job.getTargetName)
    if (target == null) {
      throw new ObjectNotFoundException(s"could not found target - ${job.getTargetName}")
    }
    EtlTargetRegistry.registerTarget(target)
    DataSourceRegistry.registerDataSource(this.lookupDataSource(target.dataSourceName()))
  }

  private def lookupDataSource(dataSourceName: String) = {
    val ds = etlRepositroty.lookupDataSource(dataSourceName)
    if (ds == null) {
      throw new ObjectNotFoundException(s"could not found data source - $dataSourceName")
    }
    ds
  }
}
