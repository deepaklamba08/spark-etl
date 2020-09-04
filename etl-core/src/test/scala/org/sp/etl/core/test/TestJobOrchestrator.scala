package org.sp.etl.core.test

import org.apache.log4j.{Level, Logger}
import org.scalatest.FunSuite
import org.sp.etl.core.model.executor.JobOrchestrator
import org.sp.etl.core.repo.impl.LocalFsEtlRepositroty

import scala.collection.JavaConverters._

class TestJobOrchestrator extends FunSuite {

  Logger.getLogger("org").setLevel(Level.OFF)
  Logger.getLogger("akka").setLevel(Level.OFF)
  private val parameters = Map("jobConfigFile" -> "src/test/resources/etl_repo/job/job_config.json",
    "dbConfigFile" -> "src/test/resources/etl_repo/ds/ds.json",
    "sourceConfigFile" -> "src/test/resources/etl_repo/etl_sources/sources.json",
    "targetConfigFile" -> "src/test/resources/etl_repo/etl_targets/targets.json",
    "objectConfigFile" -> "src/test/resources/etl_repo/objects/object_config.json")

  test("run a job with single dataset from repository") {
    val repository = new LocalFsEtlRepositroty(parameters.asJava)

    new JobOrchestrator(repository).executeJob("single-dataset-job")
  }

  test("run a job with multiple dataset from repository") {
    val repository = new LocalFsEtlRepositroty(parameters.asJava)

    new JobOrchestrator(repository).executeJob("multiple-dataset-job")
  }
  test("run a job with multiple steps from repository") {
    val repository = new LocalFsEtlRepositroty(parameters.asJava)

    new JobOrchestrator(repository).executeJob("multiple-steps-job")
  }
}
