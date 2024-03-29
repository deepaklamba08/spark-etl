package org.sp.etl.app

import org.sp.etl.common.exception.EtlExceptions
import org.sp.etl.common.repo.EtlRepository
import org.sp.etl.core.repo.impl.FsEtlRepository
import org.sp.etl.core.util.Constants
import org.sp.etl.common.repo.RepositoryParameter
import scala.collection.JavaConverters._
import scala.language.dynamics


case class EtlArgs(jobName: String, otherParameters: Map[String, String]) extends Dynamic {
  require(jobName != null && !jobName.isEmpty,
    s"job name can not be null or empty. jobName - $jobName")

  def selectDynamic(name: String): String = otherParameters(name)

  def selectRepositoryParameters =
    this.otherParameters.filter(_._1.startsWith(Constants.REPOSITORY_PREFIX)).map(p => (p._1.substring(5, p._1.length), p._2))

  def appConfigName = this.otherParameters.get(Constants.APP_CONFIG).fold(
    throw new IllegalStateException(s"could not find ${Constants.APP_CONFIG} in arguments"))(c => c)
}

class EtlArgsBuilder() {
  private var jobName: String = _
  private val otherParameters = scala.collection.mutable.Map[String, String]()


  def withJobName(_jobName: String): EtlArgsBuilder = {
    this.jobName = _jobName
    this
  }

  def withParam(paramName: String, value: String): EtlArgsBuilder = {
    this.otherParameters.put(paramName, value)
    this
  }

  def build: EtlArgs = {
    EtlArgs(this.jobName, this.otherParameters.toMap)
  }

}

object ObjectProvider {
  def createRepository(parameters: Map[String, String]): EtlRepository = {
    parameters(Constants.REPOSITORY_TYPE_KEY) match {
      case Constants.REPOSITORY_TYPE_LOCAL_FS => new FsEtlRepository(new RepositoryParameter(parameters.asJava))
      case other => throw new EtlExceptions.InvalidConfigurationException(s"repository type not supported - $other")
    }
  }
}
