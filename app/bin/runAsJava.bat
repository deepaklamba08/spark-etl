@echo off
set ETL_APP_HOME_DIR=%cd%\..\

set CLASSPATH=%CLASSPATH%;%ETL_APP_HOME_DIR%lib\*
set REPOSITORY_ARGS=repo-type localFs repo-jobConfigFile %ETL_APP_HOME_DIR%conf\etl_repo\job\job_config.json repo-dbConfigFile %ETL_APP_HOME_DIR%conf\etl_repo\ds\ds.json repo-sourceConfigFile %ETL_APP_HOME_DIR%conf\etl_repo\etl_sources\sources.json repo-targetConfigFile  %ETL_APP_HOME_DIR%conf\etl_repo\etl_targets\targets.json repo-objectConfigFile  %ETL_APP_HOME_DIR%conf\etl_repo\objects\object_config.json
set APP_ARGS=jobName %1 %REPOSITORY_ARGS%
set JVM_ARGS=-DETL_APP_HOME_DIR=%ETL_APP_HOME_DIR% -Dlog4j.configuration=file:%ETL_APP_HOME_DIR%conf\log4j.xml
echo starting etl app for configuration `%1`...

java -classpath %CLASSPATH% %JVM_ARGS% org.sp.etl.app.ETLApp %APP_ARGS%
