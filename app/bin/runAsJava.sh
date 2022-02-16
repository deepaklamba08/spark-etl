#!/bin/bash
ETL_APP_AME=$1

ETL_APP_HOME_DIR=$PWD/..
echo "etl app home directory is set to - $ETL_APP_HOME_DIR"

CLASSPATH=$CLASSPATH$:$ETL_APP_HOME_DIR/lib/*

REPOSITORY_ARGS="repo-configurationType json repo-type localFs repo-jobConfigFile $ETL_APP_HOME_DIR/conf/etl_repo/job/job_config.json repo-dbConfigFile $ETL_APP_HOME_DIR/conf/etl_repo/ds/ds.json repo-sourceConfigFile $ETL_APP_HOME_DIR/conf/etl_repo/etl_sources/sources.json repo-targetConfigFile  $ETL_APP_HOME_DIR/conf/etl_repo/etl_targets/targets.json repo-objectConfigFile  $ETL_APP_HOME_DIR/conf/etl_repo/objects/object_config.json"
echo "repository arguments set to - $REPOSITORY_ARGS"

APP_ARGS="jobName $ETL_APP_AME $REPOSITORY_ARGS"
echo "etl app arguments set to - $APP_ARGS"

JVM_ARGS="-DETL_APP_HOME_DIR=$ETL_APP_HOME_DIR -Dlog4j.configuration=file:$ETL_APP_HOME_DIR/conf/log4j.xml"
echo "starting etl app for configuration - `$1`"

java -classpath $CLASSPATH $JVM_ARGS org.sp.etl.app.ETLApp $APP_ARGS
echo "exiting shell"

