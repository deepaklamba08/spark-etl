package org.sp.etl.core.model

import org.apache.spark.sql.DataFrame
import org.sp.etl.common.ds.DataSource
import org.sp.etl.common.exception.EtlExceptions.ObjectNotFoundException
import org.sp.etl.common.io.source.EtlSource
import org.sp.etl.common.io.tr.EtlTarget
import org.sp.etl.function.EtlFunction

case class DataBag(name: String, alias: String, dataset: DataFrame)

class Databags(databags: List[DataBag]) {

  def getDatabags = databags

  def getDatabag(name: String): DataBag = databags.find(_.name.equals(name)).fold(throw new ObjectNotFoundException(s"daaset does not exist - ${name}"))(d => d)

}

object Databags {
  def apply(databags: List[DataBag]): Databags = new Databags(databags)

  def emptyDatabag = new Databags(List.empty)

}

case class Transformation(functions: List[EtlFunction], primary: DataBag, secondary: Databags)

class Registry[T] {

  private val elements = scala.collection.mutable.HashMap[String, T]()

  def register(element: T, fx: T => String) = {
    this.elements.put(fx(element), element)
  }

  @throws[ObjectNotFoundException]
  def lookup(identifier: String): T = {
    this.elements.getOrElse(identifier, throw new ObjectNotFoundException(s"could not find object - ${identifier}"))
  }
}

object EtlSourceRegistry {

  private val registry = new Registry[EtlSource]()

  def registerSource(source: EtlSource): Unit = {
    val fx: EtlSource => String = e => e.sourceName()
    this.registry.register(source, fx)
  }

  def lookupSource(sourceName: String): EtlSource = this.registry.lookup(sourceName)

}

object EtlTargetRegistry {

  private val registry = new Registry[EtlTarget]()

  def registerTarget(target: EtlTarget): Unit = {
    val fx: EtlTarget => String = e => e.targetName()
    this.registry.register(target, fx)
  }

  def lookupTarget(targetName: String): EtlTarget = this.registry.lookup(targetName)

}

object DataSourceRegistry {

  private val registry = new Registry[DataSource]()

  def registerDataSource(dataSource: DataSource): Unit = {
    val fx: DataSource => String = e => e.dataSourceName()
    this.registry.register(dataSource, fx)
  }

  def lookupDataSource(dataSourceName: String): DataSource = this.registry.lookup(dataSourceName)

}

sealed trait Status

case object SuccessStatus extends Status

case object FailedStatus extends Status
