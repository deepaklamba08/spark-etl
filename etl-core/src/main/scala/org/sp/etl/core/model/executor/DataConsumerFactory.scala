package org.sp.etl.core.model.executor

import org.sp.etl.core.model.executor.sp.data.consumer.SparkDataConsumer
import org.sp.etl.core.util.Constants

object DataConsumerFactory {

  def createDataConsumer(executorType: String): DataConsumer = {
    executorType match {
      case Constants.SPARK_JOB_EXECUTOR => new SparkDataConsumer
      case other => throw new UnsupportedOperationException(s"data consumer not supported -  $other")
    }
  }

}
