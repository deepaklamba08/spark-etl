package org.sp.etl.common.util;

public class EtlConstants {

    public static final String JOB_CONF_FILE_KEY = "jobConfigFile";
    public static final String DB_CONF_FILE_KEY = "dbConfigFile";
    public static final String SOURCE_CONF_FILE_KEY = "sourceConfigFile";
    public static final String TARGET_CONF_FILE_KEY = "targetConfigFile";
    public static final String OBJECT_CONF_FILE_KEY = "objectConfigFile";

    public static final String TYPE_FIELD = "type";

    public static final String DS_TYPE_FILE_SYSTEM = "fileSystem";
    public static final String ID_FIELD = "id";
    public static final String NAME_FIELD = "name";
    public static final String DESCRIPTION_FIELD = "description";
    public static final String ACTIVE_FIELD = "isActive";
    public static final String NAMED_PATH_FIELD = "namedPath";
    public static final String CONFIGURATION_FIELD = "configuration";
    public static final String CONFIGURATION_TYPE_FIELD = "configurationType";
    public static final String ETL_SOURCE_FILE_FORMAT_FIELD = "format";
    public static final String ETL_SOURCE_LOCATION_NAME_FIELD = "locationName";

    public static final String DS_NAME_FIELD = "dataSourceName";
    public static final String ETL_TARGET_TYPE_FILE_SYSTEM = "fileSystem";
    public static final String ETL_TARGET_SAVE_MODE_FIELD = "saveMode";
    public static final String ETL_TARGET_FORMAT_FIELD = "format";
    public static final String ETL_TARGET_LOCATION_NAME_FIELD = "locationName";


    public static final String ETL_SOURCE_TYPE_FILE_SYSTEM = "fileSystem";
    public static final String ETL_SOURCE_ALIAS = "alias";


    public static final String ETL_JOB_TARGETS_FIELD = "targets";
    public static final String ETL_JOB_STEPS_FIELD = "steps";

    public static final String ETL_STEP_SOURCES_FIELD = "sources";
    public static final String ETL_STEP_INDEX_FIELD = "stepIndex";
    public static final String ETL_STEP_OP_SOURCE_NAME_FIELD = "outputSourceName";
    public static final String ETL_STEP_OP_SOURCE_ALIAS_FIELD = "outputSourceAlias";
    public static final String ETL_STEP_PRIMARY_SOURCE_FIELD = "primarySource";
    public static final String ETL_STEP_FUNCTIONS_FIELD = "functions";


    public static final String ETL_RENAME_COLUMN_FUNCTION = "renameColumnFunction";
    public static final String ETL_ADD_CONSTANT_VALUE_FUNCTION = "addConstantValueFunction";
    public static final String ETL_DROP_COLUMN_FUNCTION = "dropColumnColumnFunction";
    public static final String ETL_REPARTITION_DATASET_FUNCTION = "repartitionDatasetFunction";
    public static final String ETL_PERSIST_DATASET_FUNCTION = "persistDatasetFunction";
    public static final String ETL_UN_PERSIST_DATASET_FUNCTION = "unPersistDatasetFunction";
    public static final String ETL_GROUP_BY_DATASET_FUNCTION = "groupByDatasetFunction";
    public static final String ETL_DATASET_UNION_FUNCTION = "datasetUnionFunction";
    public static final String ETL_DATASET_REGISTER_AS_TABLE_FUNCTION = "datasetRegisterAsTableFunction";
    public static final String ETL_FILTER_DATASET_FUNCTION = "filterDatasetFunction";
    public static final String ETL_SORT_DATASET_FUNCTION = "sortDatasetFunction";
    public static final String ETL_INNER_JOIN_DATASET_FUNCTION = "innerJoinDatasetFunction";
    public static final String ETL_LEFT_JOIN_DATASET_FUNCTION = "leftJoinDatasetFunction";
    public static final String ETL_RIGHT_JOIN_DATASET_FUNCTION = "rightJoinDatasetFunction";
    public static final String ETL_CURRENT_DATE_FUNCTION = "currentDateFunction";
    public static final String ETL_CURRENT_TIMESTAMP_FUNCTION = "currentTimestampFunction";
    public static final String ETL_TO_DATE_FUNCTION = "toDateFunction";
    public static final String ETL_TO_TIMESTAMP_FUNCTION = "toTimestampFunction";
    public static final String ETL_SUM_COLUMN_FUNCTION = "sumColumnFunction";
    public static final String ETL_CAST_COLUMN_FUNCTION = "castColumnFunction";
    public static final String ETL_SQL_DATASET_FUNCTION = "sqlFunction";


    public static final String OLD_NAME_FIELD = "oldName";
    public static final String NEW_NAME_FIELD = "newName";
    public static final String COLUMN_NAME_FIELD = "columnName";
    public static final String VALUE_FIELD = "value";
    public static final String REPARTITION_COLUMNS_FIELD = "partitionColumns";
    public static final String NUM_PARTITIONS_FIELD = "numPartitions";
    public static final String PERSIST_LEVEL_FIELD = "persistLevel";
    public static final String GROUP_BY_COLUMNS_FIELD = "groupByColumns";
    public static final String AGGREGATE_VALUES_FIELD = "aggregateValues";
    public static final String AGGREGATE_VALUE_MAX_FIELD = "aggregateValueMax";
    public static final String AGGREGATE_VALUE_SUM_FIELD = "aggregateValueSumValue";
    public static final String AGGREGATE_VALUE_RESULT_ALIAS_FIELD = "resultAlias";
    public static final String SECOND_DATASET_NAME_FIELD = "secondDatasetName";
    public static final String UNION_BY_NAME_FIELD = "unionByName";
    public static final String DATASET_NAME_FIELD = "datasetName";
    public static final String FILTER_CONDITION_FIELD = "filterCondition";
    public static final String SORT_COLUMNS_FIELD = "sortColumns";
    public static final String FORMAT_FIELD = "format";
    public static final String SOURCE_COLUMN_FIELD = "sourceColumn";
    public static final String RESULT_COLUMN_NAME_FIELD = "resultColumnName";
    public static final String COLUMNS_FIELD = "columns";
    public static final String TO_TYPE_FIELD = "toType";
    public static final String LEFT_DATASET_NAME_FIELD = "leftDatasetName";
    public static final String RIGHT_DATASET_NAME = "rightDatasetName";
    public static final String LEFT_DATASET_COLUMN_NAME_FIELD = "leftDatasetColumn";
    public static final String RIGHT_DATASET_COLUMN_NAME_FIELD = "rightDatasetColumn";
    public static final String QUERY_TYPE_FIELD = "queryType";
    public static final String QUERY_SOURCE_FIELD = "querySource";

}
