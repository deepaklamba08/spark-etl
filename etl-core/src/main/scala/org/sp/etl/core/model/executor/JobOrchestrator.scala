package org.sp.etl.core.model.executor

import org.slf4j.LoggerFactory
import org.sp.etl.common.exception.EtlExceptions
import org.sp.etl.common.exception.EtlExceptions.{EtlAppException, ObjectNotFoundException}
import org.sp.etl.common.model.job.Job
import org.sp.etl.common.ds.DataSource
import org.sp.etl.common.repo.{EtlRepository, IJobExecution, JobExecutionDetail, JobExecutionStatus}
import org.sp.etl.core.model._
import org.sp.etl.core.util.Constants
import org.sp.etl.common.model.Id

import java.util.Date
import scala.collection.JavaConverters._

class JobOrchestrator(etlRepository: EtlRepository, jobExecution: IJobExecution) {
  private val logger = LoggerFactory.getLogger(this.getClass)
  private val statusMapping: Map[Status, JobExecutionStatus] = Map(SuccessStatus -> JobExecutionStatus.Completed,
    FailedStatus -> JobExecutionStatus.Failed)

  def executeJob(jobName: String) = {
    logger.debug("executing - JobOrchestrator.executeJob()")
    val job = this.lookupJob(jobName)
    logger.debug(s"loaded job ${job} from repository")

    val executionId = this.jobExecution.startJobExecution(new JobExecutionDetail(job.getName,
      JobExecutionStatus.Running, new Date(), ""))

    try {
      this.registerSources(job)
      val executor = JobExecutorFactory.createJobExecutor(job.getName, Constants.SPARK_JOB_EXECUTOR, this.etlRepository.lookupObject(job.getConfigValue(Constants.EXECUTOR_CONF_NAME)))
      val jobExecutionResult = executor.executeJob(job)
      jobExecutionResult.status match {
        case SuccessStatus => this.storeResultDataset(job.getTargets.asScala, jobExecutionResult.dataBag)
        case FailedStatus => logger.error(s"job execution failed, cause - ${jobExecutionResult.executionMessage}")
      }
      this.jobExecution.updateJobExecution(executionId, this.statusMapping(jobExecutionResult.status),
        jobExecutionResult.executionMessage)
    } catch {
      case ae: Exception => logger.error("etl application error occurred while running job", ae)
        this.logFailure(executionId, ae)
    }
  }

  private def lookupJob(jobName: String): Job = {
    val etlJob = this.etlRepository.lookupJob(jobName)

    if (etlJob == null) {
      throw new EtlExceptions.ObjectNotFoundException(s"could not find job $jobName in the repository")
    } else if (!etlJob.isActive) {
      throw new IllegalStateException(s"job ${jobName} is disabled in the repository")
    } else {
      etlJob
    }
  }

  private def registerSources(job: Job): Unit = {
    val sources = job.getSteps.asScala.flatMap(_.getSources.asScala).toSet.map(etlRepository.lookupEtlSource).filter(p => p != null)
    sources.foreach(EtlSourceRegistry.registerSource)
    sources.map(_.dataSourceName()).map(this.lookupDataSource).foreach(DataSourceRegistry.registerDataSource)

    job.getTargets.asScala.map(etlRepository.lookupEtlTarget)
      .foreach(target => {
        EtlTargetRegistry.registerTarget(target)
        DataSourceRegistry.registerDataSource(this.lookupDataSource(target.dataSourceName()))
      })
  }

  private def lookupDataSource(dataSourceName: String): DataSource = {
    val ds = etlRepository.lookupDataSource(dataSourceName)
    if (ds == null) {
      throw new ObjectNotFoundException(s"could not found data source - $dataSourceName")
    }
    ds
  }

  private def storeResultDataset(targets: Seq[String], dataset: DataBag): Unit = {
    targets.foreach(targetName => {
      val etlTarget = EtlTargetRegistry.lookupTarget(targetName)
      DataConsumerFactory.createDataConsumer(Constants.SPARK_JOB_EXECUTOR).consume(dataset, etlTarget)
    })
  }

  private def logFailure(executionId: Id, cause: Throwable): Unit = {
    val message = cause match {
      case ae: EtlAppException => s"Etl app exception - $ae"
      case other => s"Unknown exception - $other"
    }
    this.jobExecution.updateJobExecution(executionId, JobExecutionStatus.Failed, message)
  }
}
