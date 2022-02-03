package org.sp.etl.core.test

import java.util
import org.apache.log4j.{Level, Logger}
import org.scalatest.FunSuite
import org.sp.etl.common.ds.LocalFileSystemDataSource
import org.sp.etl.common.io.source.impl.CsvFileEtlSource
import org.sp.etl.common.model.JsonConfiguration
import org.sp.etl.common.model.job.Job
import org.sp.etl.common.model.step.Step
import org.sp.etl.core.model.executor.sp.SparkJobExecutor
import org.sp.etl.core.model.{DataSourceRegistry, EtlSourceRegistry, SuccessStatus}
import org.sp.etl.function.column.DateAndTimeFunction.CurrentDateFunction
import org.sp.etl.function.column.agg.{GroupByDatasetFunction, SumValue}
import org.sp.etl.function.column.math.SumColumnFunction
import org.sp.etl.function.column.{AddConstantValueFunction, CastColumnFunction, RenameColumnFunction}

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

    val dataSource = new LocalFileSystemDataSource("src/test/resources/test_data", "local-ds")
    DataSourceRegistry.registerDataSource(dataSource)

    val etlSource = new CsvFileEtlSource
    etlSource.setName("marks_data")
    etlSource.setAlias("marks_data")
    etlSource.setDataSourceName("local-ds")
    etlSource.setFileName("student_marks.csv")
    val conf = new util.HashMap[String, String]()
    conf.put("header", "true")
    etlSource.setConfig(conf)

    EtlSourceRegistry.registerSource(etlSource)

    val sumMarks = new SumValue("marks", "total_marks")
    val groupByFunction = new GroupByDatasetFunction("group_marks", "group_marks", util.Arrays.asList("student_id"), util.Arrays.asList(sumMarks))
    val renameColumnFunction = new RenameColumnFunction("rename column", "rename column", "total_marks", "total_marks_of_student")
    val calculationDateFunction = new CurrentDateFunction("calculation date", "calculation date", "calculation_date")
    val constValue = new AddConstantValueFunction("add a constant value", "add a constant value", "constant_value", 10)
    val castValue = new CastColumnFunction("cast value", "cast value", "marks_int", "total_marks_of_student", "int")
    val addColumnValues = new SumColumnFunction("add column values", "add column values", "added_values", util.Arrays.asList("marks_int", "constant_value"))

    val step = new Step()
    step.setStepName("aggregate-marks")
    step.setStepIndex(0)
    step.setSources(util.Arrays.asList("marks_data"))
    step.setOutputSourceName("marks_aggregated")
    step.setOutputSourceAlias("marks_aggregated")
    step.setInputSourceName("marks_data")
    step.setEtlFunctions(util.Arrays.asList(groupByFunction, renameColumnFunction, calculationDateFunction, constValue, castValue, addColumnValues))

    val etlJob = new Job()
    etlJob.setJobName("student_data_analysis")
    etlJob.setSteps(util.Arrays.asList(step))

    val result = jobExecutor.executeJob(etlJob)

    assert(result.status == SuccessStatus)
    result.dataBag.dataset.printSchema()
    result.dataBag.dataset.show()
  }

}
