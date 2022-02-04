package org.sp.etl.core.model.executor.sp

import org.sp.etl.core.model.executor.sp.function.{ColumnFunctionRunnerFactory, DatasetFunctionRunnerFactory}
import org.sp.etl.core.model.executor.{FunctionExecutionResult, TransformationRunner}
import org.sp.etl.core.model.{DataBag, Databags, FailedStatus}
import org.sp.etl.function.EtlFunction
import org.sp.etl.function.column.ColumnFunction
import org.sp.etl.function.DatasetFunction

import scala.util.{Failure, Success, Try}

class SparkTransformationRunner extends TransformationRunner {

  override protected def executeFunction(etlFunction: EtlFunction, primary: DataBag, secondary: Databags): FunctionExecutionResult = {
    Try(runFunction(etlFunction, primary, secondary)) match {
      case Success(databag) => FunctionExecutionResult(databag)
      case Failure(cause) => FunctionExecutionResult(null, cause.getMessage, FailedStatus)
    }
  }

  private def runFunction(etlFunction: EtlFunction, primary: DataBag, secondary: Databags) = {
    etlFunction match {
      case cFx: ColumnFunction => ColumnFunctionRunnerFactory.getFunctionRunner(cFx).run(primary)
      case dFx: DatasetFunction => DatasetFunctionRunnerFactory.getFunctionRunner(dFx).run(primary, secondary)
    }
  }

}
