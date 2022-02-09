package org.sp.etl.app

import org.slf4j.LoggerFactory
import org.sp.etl.core.model.executor.JobOrchestrator
import org.sp.etl.core.util.Constants

import scala.util.{Try, Success, Failure}

object ETLApp {
  private lazy val logger = LoggerFactory.getLogger(this.getClass)

  def main(args: Array[String]): Unit = {
    logger.debug("starting etl app")
    val etlArgs = this.parseArgs(args)

    val etlRepo = RepositoryProvider.createRepository(etlArgs.selectRepositoryParameters)
    Try(new JobOrchestrator(etlRepo).executeJob(etlArgs.jobName)) match {
      case Success(_) =>
      case Failure(cause) => logger.error("error occurred while processing data", cause)
    }
    logger.debug("exiting etl app")
  }

  private def parseArgs(args: Array[String]) = {
    args.sliding(2, 2).foldLeft(new EtlArgsBuilder())((builder, pair) => {
      pair.toList match {
        case Constants.JOB_NAME :: value :: Nil => builder.withJobName(value)
        case key :: value :: Nil => builder.withParam(key, value)
        case other :: Nil => throw new IllegalArgumentException(s"can not use argument without name or value - $other")
      }
    }).build
  }

}
