package org.sp.etl.core.model.executor.sp

import org.apache.spark.sql.SparkSession
import org.sp.etl.common.model.Configuration

import scala.collection.JavaConverters._

object SparkHelper {

  private final val SPARK_CONFIG_KEY = "sparkConfig"

  def createSparkSession(appName: String, sparkConfig: Configuration) = {
    val sessionBuilder = SparkSession.builder().appName(appName)
    sparkConfig.getValueMap(SPARK_CONFIG_KEY).asScala.foldLeft(sessionBuilder)((builder, conf) => builder.config(conf._1, conf._2)).getOrCreate()
  }
}
