package org.sp.etl.core.test


import java.util
import org.apache.log4j.{Level, Logger}
import org.scalatest.FunSuite
import org.sp.etl.common.model.{ConfigurationType, JsonConfiguration, StringId}
import org.sp.etl.common.model.job.Job
import org.sp.etl.common.model.step.Step
import org.sp.etl.core.model.executor.sp.SparkJobExecutor
import org.sp.etl.core.model.{DataSourceRegistry, EtlSourceRegistry, SuccessStatus}
import org.sp.etl.function.column.DateAndTimeFunction.CurrentDateFunction
import org.sp.etl.function.column.agg.{GroupByDatasetFunction, SumValue}
import org.sp.etl.function.column.{AddConstantValueFunction, CastColumnFunction, RenameColumnFunction}
import org.sp.etl.function.column.math.SumColumnFunction
import org.sp.etl.common.ds.FileSystemDataSource
import org.sp.etl.common.io.source.impl.FileEtlSource
import org.sp.etl.common.util.{ConfigurationFactory, EtlConstants}
import org.sp.etl.function.dataset.{DatasetRegisterAsTableFunction, SQLFunction}

import scala.collection.JavaConverters._

class TestJobExecutor extends FunSuite {

  Logger.getLogger("org").setLevel(Level.OFF)
  Logger.getLogger("akka").setLevel(Level.OFF)
  private val jobExecutor = this.createJobExecutor

  private def createJobExecutor = {
    val sparkConf = JsonConfiguration.fromString(
      """{
        |  "objectName": "executorConf",
        |  "sparkConfig": {
        |    "spark.master": "local[2]",
        |    "spark.testing.memory": "471859200"
        |  }
        |}""".stripMargin)
    new SparkJobExecutor("TestJobExecutor", sparkConf)
  }

  test("test group by function") {

    val dataSource = new FileSystemDataSource.Builder()
      .withName("file_ds")
      .withDescription("file data source")
      .makeActive()
      .withNamedPaths(Map("student_marks" -> "etl-core/src/test/resources/test_data/student_marks.csv").asJava)
      .build()
    DataSourceRegistry.registerDataSource(dataSource)

    val configMap: Map[String, AnyRef] = Map(EtlConstants.READER_CONFIG -> Map("header" -> "true").asJava)
    val sourceConfig = ConfigurationFactory.fromMap(configMap.asJava, ConfigurationType.JSON)

    val etlSource = new FileEtlSource.Builder()
      .withName("marks_data")
      .makeActive()
      .withAlias("marks_data")
      .withDataSourceName("file_ds")
      .withFileFormat("csv")
      .withLocationName("student_marks")
      .withConfiguration(sourceConfig)
      .build()

    EtlSourceRegistry.registerSource(etlSource)

    val sumMarks = new SumValue("marks", "total_marks")
    val groupByFunction = new GroupByDatasetFunction(new StringId("1"), "group_marks", "group_marks", true, util.Arrays.asList("student_id"), util.Arrays.asList(sumMarks))
    val renameColumnFunction = new RenameColumnFunction(new StringId("2"), "rename column", "rename column", true, "total_marks", "total_marks_of_student")
    val calculationDateFunction = new CurrentDateFunction(new StringId("3"), "calculation date", "calculation date", true, "calculation_date", null)
    val constValue = new AddConstantValueFunction(new StringId("4"), "add a constant value", "add a constant value", true, "constant_value", 10)
    val castValue = new CastColumnFunction(new StringId("5"), "cast value", "cast value", true, "marks_int", "total_marks_of_student", "int")
    val addColumnValues = new SumColumnFunction(new StringId("6"), "add column values", "add column values", true, "added_values", util.Arrays.asList("marks_int", "constant_value"))

    val registerTable = new DatasetRegisterAsTableFunction(new StringId("7"), "create table", "create table", true, "marks_table")
    val sqlFunction = new SQLFunction(new StringId("8"), "sql function", "execute sql query", true, EtlConstants.QUERY_TYPE_SQL, "select * from marks_table")

    val step = new Step.Builder()
      .withName("aggregate-marks")
      .makeActive()
      .withStepIndex(0)
      .withSource("marks_data")
      .withOutputSourceName("marks_aggregated")
      .withOutputSourceAlias("marks_aggregated")
      .withPrimarySource("marks_data")
      .withEtlFunction(groupByFunction)
      .withEtlFunction(renameColumnFunction)
      .withEtlFunction(calculationDateFunction)
      .withEtlFunction(constValue)
      .withEtlFunction(castValue)
      .withEtlFunction(addColumnValues)
      .withEtlFunction(registerTable)
      .withEtlFunction(sqlFunction)
      .build()

    val etlJob = new Job.Builder()
      .withName("student_data_analysis")
      .makeActive()
      .withStep(step)
      .build()

    val result = jobExecutor.executeJob(etlJob)

    assert(result.status == SuccessStatus)
    result.dataBag.dataset.printSchema()
    result.dataBag.dataset.show()
  }

}

