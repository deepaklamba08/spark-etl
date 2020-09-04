package org.sp.etl.core.model.executor.sp.data.consumer

import java.util

import org.apache.spark.sql.{DataFrameWriter, Row}
import org.sp.etl.common.ds.FileSystemDataSource
import org.sp.etl.common.exception.EtlExceptions.InvalidConfigurationException
import org.sp.etl.common.io.tr.EtlTarget
import org.sp.etl.common.io.tr.impl.FileEtlTarget
import org.sp.etl.core.model.executor.DataConsumer
import org.sp.etl.core.model.{DataBag, DataSourceRegistry}

import scala.collection.JavaConverters._


class SparkDataConsumer extends DataConsumer {

  override def consume(databag: DataBag, target: EtlTarget): Unit = {
    target match {
      case fs: FileEtlTarget => this.storeFileSystemData(databag, fs)
      case other => throw new UnsupportedOperationException(s"target not supported - ${other.getClass}")
    }
  }

  private def storeFileSystemData(databag: DataBag, fsTarget: FileEtlTarget) = {
    val targetDs = DataSourceRegistry.lookupDataSource(fsTarget.dataSourceName())
    if (!targetDs.isInstanceOf[FileSystemDataSource]) {
      throw new InvalidConfigurationException("data source type is invalid")
    }
    val fileDs = targetDs.asInstanceOf[FileSystemDataSource]
    val writer = databag.dataset.write.format(fsTarget.fileFormat())
    this.setConfig(writer, fsTarget.config()).save(fileDs.getPath(fsTarget.getFileName))
  }

  private def setConfig(writer: DataFrameWriter[Row], config: util.Map[String, String]) = {
    if (config != null) {
      config.asScala.foldLeft(writer)((builder, pair) => builder.option(pair._1, pair._2))
    } else {
      writer
    }
  }
}
