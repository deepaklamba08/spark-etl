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

    val etlRepo = ObjectProvider.createRepository(etlArgs.selectRepositoryParameters)
    Try(new JobOrchestrator(etlRepo, null).executeJob(etlArgs.jobName)) match {
      case Success(_) => logger.debug("completed execution...")
      case Failure(cause) => logger.error("error occurred while processing data", cause)
    }
    logger.debug("exiting etl app")
  }

  private def parseArgs(args: Array[String]) = {
    if (args.length % 2 != 0) {
      throw new IllegalArgumentException(s"arguments should be in key value pairs, no. of arguments must be even")
    }
    args.sliding(2, 2).foldLeft(new EtlArgsBuilder())((builder, pair) => {
      pair.toList match {
        case Constants.JOB_NAME :: value :: Nil => builder.withJobName(value)
        case key :: value :: Nil => builder.withParam(key, value)
        case other :: Nil => throw new IllegalArgumentException(s"can not use argument without name or value - $other")
      }
    }).build
  }


}
