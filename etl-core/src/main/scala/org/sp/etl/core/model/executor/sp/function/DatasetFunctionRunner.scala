package org.sp.etl.core.model.executor.sp.function

import org.apache.spark.sql.DataFrame
import org.sp.etl.core.model.{DataBag, Databags}
import org.sp.etl.function.dataset._
import org.sp.etl.function.DatasetFunction
import org.sp.etl.function.dataset.{DatasetJoinFunction, DatasetRegisterAsTableFunction, DatasetUnionFunction, InnerJoinDatasetFunction, LeftJoinDatasetFunction, RightJoinDatasetFunction}

abstract class DatasetFunctionRunner(function: DatasetFunction) extends FunctionRunner[DatasetFunction](function) {
  def run(dataBag: DataBag, databags: Databags): DataBag
}

object DatasetFunctionRunnerFactory {

  def getFunctionRunner(function: DatasetFunction): DatasetFunctionRunner =
    function match {
      case ij: InnerJoinDatasetFunction => new InnerJoinDatasetFunctionRunner(ij)
      case lj: LeftJoinDatasetFunction => new LeftJoinDatasetFunctionRunner(lj)
      case rj: RightJoinDatasetFunction => new RightJoinDatasetFunctionRunner(rj)
      case un: DatasetUnionFunction => new DatasetUnionFunctionRunner(un)
      case rd: DatasetRegisterAsTableFunction => new DatasetRegisterAsTableFunctionRunner(rd)
      case un: UnPersistDatasetFunction => new UnPersistDatasetColumnFunctionRunner(un)
      case other => throw new UnsupportedOperationException(s"unsupported dataset function - ${other}")
    }
}

abstract class JoinDatasetFunctionRunner(function: DatasetJoinFunction) extends DatasetFunctionRunner(function) {

  protected def joinDatasets(leftDataset: DataFrame, leftDatasetColumn: String, rightDataset: DataFrame, rightDatasetColumn: String, joinType: String) = {
    val jc = leftDataset(leftDatasetColumn) === rightDataset(rightDatasetColumn)
    leftDataset.join(rightDataset, jc, joinType)
  }
}


class UnPersistDatasetColumnFunctionRunner(function: UnPersistDatasetFunction) extends DatasetFunctionRunner(function) {
  override def run(dataBag: DataBag, databags: Databags): DataBag = DataBag(dataBag.name, dataBag.alias, dataBag.dataset.unpersist())
}

class InnerJoinDatasetFunctionRunner(function: InnerJoinDatasetFunction) extends JoinDatasetFunctionRunner(function) {
  override def run(dataBag: DataBag, databags: Databags): DataBag = {
    val rightDs = databags.getDatabag(function.getRightDatasetName).dataset
    val opDataset = this.joinDatasets(dataBag.dataset, function.getLeftDatasetColumn, rightDs, function.getRightDatasetColumn, function.joinType())
    DataBag(dataBag.name, dataBag.alias, opDataset)
  }
}

class LeftJoinDatasetFunctionRunner(function: LeftJoinDatasetFunction) extends JoinDatasetFunctionRunner(function) {
  override def run(dataBag: DataBag, databags: Databags): DataBag = {
    val rightDs = databags.getDatabag(function.getRightDatasetName).dataset
    val opDataset = this.joinDatasets(dataBag.dataset, function.getLeftDatasetColumn, rightDs, function.getRightDatasetColumn, function.joinType())
    DataBag(dataBag.name, dataBag.alias, opDataset)
  }
}


class RightJoinDatasetFunctionRunner(function: RightJoinDatasetFunction) extends JoinDatasetFunctionRunner(function) {
  override def run(dataBag: DataBag, databags: Databags): DataBag = {
    val rightDs = databags.getDatabag(function.getRightDatasetName).dataset
    val opDataset = this.joinDatasets(dataBag.dataset, function.getLeftDatasetColumn, rightDs, function.getRightDatasetColumn, function.joinType())
    DataBag(dataBag.name, dataBag.alias, opDataset)
  }
}

class DatasetUnionFunctionRunner(function: DatasetUnionFunction) extends DatasetFunctionRunner(function) {
  override def run(dataBag: DataBag, databags: Databags): DataBag = {
    val sDatabag = databags.getDatabag(function.getSecondDatasetName).dataset
    val opDataset = if (function.isUnionByName) {
      dataBag.dataset.unionByName(sDatabag)
    } else {
      dataBag.dataset.union(sDatabag)
    }
    DataBag(dataBag.name, dataBag.alias, opDataset)
  }
}

class DatasetRegisterAsTableFunctionRunner(function: DatasetRegisterAsTableFunction) extends DatasetFunctionRunner(function) {
  override def run(dataBag: DataBag, databags: Databags): DataBag = {
    dataBag.dataset.createTempView(function.getDatasetName)
    dataBag
  }
}