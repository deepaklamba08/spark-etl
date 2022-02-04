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


    public static final String DS_NAME_FIELD = "dataSourceName";
    public static final String ETL_TARGET_TYPE_FILE_SYSTEM = "fileSystem";
    public static final String ETL_TARGET_SAVE_MODE_FIELD = "saveMode";


    public static final String ETL_SOURCE_TYPE_FILE_SYSTEM = "fileSystem";
    public static final String ETL_SOURCE_ALIAS = "alias";


    public static final String ETL_JOB_TARGETS_FIELD = "targets";
    public static final String ETL_JOB_STEPS_FIELD = "steps";

    public static final String ETL_STEP_SOURCES_FIELD = "sources";
    public static final String ETL_STEP_INDEX_FIELD = "stepIndex";
    public static final String ETL_STEP_OP_SOURCE_NAME_FIELD = "outputSourceName";
    public static final String ETL_STEP_OP_SOURCE_ALIAS_FIELD = "outputSourceAlias";
    public static final String ETL_STEP_FUNCTIONS_FIELD = "functions";

    public static final String ETL_COLUMN__FUNCTION = "columnFunction";
    public static final String ETL_RENAME_COLUMN_FUNCTION = "renameColumnFunction";
    public static final String ETL_AddConstantValue_FUNCTION = "addConstantValueFunction";
    public static final String ETL_DropColumn_FUNCTION = "dropColumnColumnFunction";
    public static final String ETL_Dataset_FUNCTION = "datasetFunction";
    public static final String ETL_InnerJoinDataset_FUNCTION = "innerJoinDatasetFunction";
    public static final String ETL_RepartitionDataset_FUNCTION = "repartitionDatasetFunction";
    public static final String ETL_PersistDataset_FUNCTION = "persistDatasetFunction";
    public static final String ETL_UnPersistDataset_FUNCTION = "unPersistDatasetFunction";
    public static final String ETL_GroupByDataset_FUNCTION = "groupByDatasetFunction";
    public static final String ETL_DatasetUnion_FUNCTION = "datasetUnionFunction";
    public static final String ETL_DatasetRegisterAsTable_FUNCTION = "datasetRegisterAsTableFunction";
    public static final String ETL_FilterDataset_FUNCTION = "filterDatasetFunction";
    public static final String ETL_SortDataset_FUNCTION = "sortDatasetFunction";
    public static final String ETL_LeftJoinDataset_FUNCTION = "leftJoinDatasetFunction";
    public static final String ETL_RightJoinDataset_FUNCTION = "rightJoinDatasetFunction";
    public static final String ETL_CurrentDate_FUNCTION = "currentDateFunction";
    public static final String ETL_CurrentTimestamp_FUNCTION = "currentTimestampFunction";
    public static final String ETL_ToDate_FUNCTION = "toDateFunction";
    public static final String ETL_ToTimestamp_FUNCTION = "toTimestampFunction";
    public static final String ETL_SumColumn_FUNCTION = "sumColumnFunction";
    public static final String ETL_CastColumn_FUNCTION = "castColumnFunction";

}
