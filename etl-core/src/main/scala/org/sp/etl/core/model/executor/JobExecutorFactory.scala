package org.sp.etl.core.model.executor

import org.sp.etl.common.util.JsonDataObject
import org.sp.etl.core.model.executor.sp.SparkJobExecutor
import org.sp.etl.core.util.Constants

object JobExecutorFactory {

  def createJobExecutor(jobName: String, executorType: String, executorConfig: JsonDataObject) = {
    executorType match {
      case Constants.SPARK_JOB_EXECUTOR => new SparkJobExecutor(jobName, executorConfig)
      case other => throw new IllegalStateException(s"no such job executor - $other")
    }

  }

}
