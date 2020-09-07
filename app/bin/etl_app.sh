#!/bin/bash

source ./set_env.sh

export ETL_APP_HOME_DIR="$(dirname "$PWD")"

REPOSITORY_ARGS="repo-type localFs repo-jobConfigFile ${REPOSITORY_ARGS}/conf/etl_repo/job/job_config.json repo-dbConfigFile ${REPOSITORY_ARGS}/conf/etl_repo/db/db.json repo-sourceConfigFile ${REPOSITORY_ARGS}/conf/etl_repo/etl_sources/sources.json repo-targetConfigFile  ${REPOSITORY_ARGS}/conf/etl_repo/etl_targets/targets.json repo-objectConfigFile  ${REPOSITORY_ARGS}/conf/etl_repo/objects/object_config.json"

APP_JAR="${ETL_APP_HOME_DIR}/lib/etl-app-1.0-SNAPSHOT.jar"
APP_ARGS="jobName $1 ${REPOSITORY_ARGS}"
echo "starting etl app..."
nohup spark-submit --master $SPARK_MASTER  --deploy-mode client --supervise --executor-memory $EXECUTOR_MEMORY --driver-memory $DRIVER_MEMORY --num-executors $NUM_EXECUTORS --executor-cores $EXE_CORES --conf spark.cores.max=$EXE_CORES $APP_JAR $APP_ARGS > $ETL_APP_HOME_DIR/tmp/nohup.out &