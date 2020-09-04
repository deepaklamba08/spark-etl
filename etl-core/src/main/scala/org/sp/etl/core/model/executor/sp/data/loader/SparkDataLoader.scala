package org.sp.etl.core.model.executor.sp.data.loader

import java.util

import org.apache.spark.sql.{DataFrameReader, SparkSession}
import org.sp.etl.common.ds.{DataSource, FileSystemDataSource}
import org.sp.etl.common.io.source.EtlSource
import org.sp.etl.common.io.source.impl.FileEtlSource
import org.sp.etl.core.model.DataBag
import org.sp.etl.core.model.executor.DataLoader

import scala.collection.JavaConverters._

class SparkDataLoader(sparkSession: SparkSession) extends DataLoader {
  override def loadData(source: EtlSource, dataSource: DataSource): DataBag = {
    val dataset = source match {
      case fileSource: FileEtlSource if (dataSource.isInstanceOf[FileSystemDataSource]) => this.loadFileDataset(fileSource, dataSource.asInstanceOf[FileSystemDataSource])
      case other => throw new UnsupportedOperationException(s"source not supported - ${other.getClass}")
    }
    DataBag(source.sourceName(), source.sourceAlias(), dataset)
  }

  private def loadFileDataset(fileSource: FileEtlSource, dataSource: FileSystemDataSource) = {
    val reader = this.sparkSession.read.format(fileSource.fileFormat())
    this.setConfig(reader, fileSource.config()).load(dataSource.getPath(fileSource.getFileName))
  }

  private def setConfig(reader: DataFrameReader, config: util.Map[String, String]) = {
    if (config != null) {
      config.asScala.foldLeft(reader)((builder, pair) => builder.option(pair._1, pair._2))
    } else {
      reader
    }
  }
}
