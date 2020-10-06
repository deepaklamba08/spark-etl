package org.sp.etl.core.model.executor

import org.slf4j.LoggerFactory
import org.sp.etl.common.exception.EtlExceptions
import org.sp.etl.common.exception.EtlExceptions.{EtlAppException, ObjectNotFoundException, SystemFailureException}
import org.sp.etl.common.repo.EtlRepositroty
import org.sp.etl.core.model.{DataBag, DataSourceRegistry, EtlSourceRegistry, EtlTargetRegistry, FailedStatus, SuccessStatus}
import org.sp.etl.core.util.Constants
import org.sp.etl.common.model.job.Job
import org.sp.etl.core.moniter.IJobStatusDAO

import scala.collection.JavaConverters._

class JobOrchestrator(etlRepositroty: EtlRepositroty, statusDAO: IJobStatusDAO) {
  private val logger = LoggerFactory.getLogger(this.getClass)

  def executeJob(jobName: String) = {
    logger.debug("executing - JobOrchestrator.executeJob()")
    val job = this.lookupJob(jobName)
    logger.debug(s"loaded job ${job} from repository")
    this.registerSources(job)

    try {
      this.executeJobInternal(job)
    } catch {
      case ae: EtlAppException => logger.error("etl application error occurred while running job", ae)
      case other => logger.error("error occurred while running job", other)
    }
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
    val sources = job.getSteps.asScala.flatMap(_.getSources.asScala).toSet.map(etlRepositroty.lookupEtlSource).filter(p => p != null)
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

  private def executeJobInternal(job: Job) = {
    val executor = JobExecutorFactory.createJobExecutor(job.getJobName, Constants.SPARK_JOB_EXECUTOR, this.etlRepositroty.lookupObject(Constants.EXECUTOR_CONF_NAME), statusDAO)
    val jobExecutionId = statusDAO.startJobExecution(job.getJobName)
    val jobExecutionResult = executor.executeJob(job, jobExecutionId)
    jobExecutionResult.status match {
      case SuccessStatus => this.storeResultDataset(job.getTargetName, jobExecutionResult.dataBag)
      case FailedStatus => logger.error(s"job execution failed, cause - ${jobExecutionResult.executionMessage}")
    }
    statusDAO.endJobExecution(jobExecutionId, jobExecutionResult.status.toString, "")
  }

  private def storeResultDataset(targetName: String, dataset: DataBag) = {
    val etlTarget = EtlTargetRegistry.lookupTarget(targetName)
    DataConsumerFactory.createDataConsumer(Constants.SPARK_JOB_EXECUTOR).consume(dataset, etlTarget)
  }
}
