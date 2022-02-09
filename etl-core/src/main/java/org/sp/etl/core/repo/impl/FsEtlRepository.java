package org.sp.etl.core.repo.impl;

import org.sp.etl.common.ds.DataSource;
import org.sp.etl.common.ds.FileSystemDataSource;
import org.sp.etl.common.exception.EtlExceptions;
import org.sp.etl.common.io.source.EtlSource;
import org.sp.etl.common.io.source.impl.FileEtlSource;
import org.sp.etl.common.io.tr.EtlTarget;
import org.sp.etl.common.io.tr.impl.FileEtlTarget;
import org.sp.etl.common.model.*;
import org.sp.etl.common.model.job.Job;
import org.sp.etl.common.model.step.Step;
import org.sp.etl.common.repo.EtlRepository;
import org.sp.etl.common.repo.RepositoryType;
import org.sp.etl.common.util.ConfigurationFactory;
import org.sp.etl.common.util.EtlConstants;
import org.sp.etl.common.util.Preconditions;
import org.sp.etl.function.EtlFunction;
import org.sp.etl.function.column.*;
import org.sp.etl.function.column.agg.AggregateValue;
import org.sp.etl.function.column.agg.GroupByDatasetFunction;
import org.sp.etl.function.column.agg.MaxValue;
import org.sp.etl.function.column.agg.SumValue;
import org.sp.etl.function.column.math.SumColumnFunction;
import org.sp.etl.function.dataset.*;


import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FsEtlRepository implements EtlRepository {

    private final DataStore<Job> jobDataStore;
    private final DataStore<Configuration> jsonDataObjectDataStore;
    private final DataStore<EtlSource> etlSourceDataStore;
    private final DataStore<DataSource> dataSourceDataStore;
    private final DataStore<EtlTarget> etlTargetDataStore;

    public FsEtlRepository(Map<String, String> parameters) {
        ConfigurationType configurationType = ConfigurationType.getConfigurationType(parameters.get(EtlConstants.CONFIGURATION_TYPE_FIELD));
        this.jobDataStore = new DataStore<>(configurationType, this.getPath(EtlConstants.JOB_CONF_FILE_KEY, parameters), ConfigMapper::mapJob, Job::getName);
        this.jsonDataObjectDataStore = new DataStore<>(configurationType, this.getPath(EtlConstants.OBJECT_CONF_FILE_KEY, parameters), Function.identity(), Identifiable::getName);
        this.etlSourceDataStore = new DataStore<>(configurationType, this.getPath(EtlConstants.SOURCE_CONF_FILE_KEY, parameters), ConfigMapper::mapEtlSource, Identifiable::getName);
        this.dataSourceDataStore = new DataStore<>(configurationType, this.getPath(EtlConstants.DB_CONF_FILE_KEY, parameters), ConfigMapper::mapDataSource, Identifiable::getName);
        this.etlTargetDataStore = new DataStore<>(configurationType, this.getPath(EtlConstants.TARGET_CONF_FILE_KEY, parameters), ConfigMapper::mapEtlTarget, Identifiable::getName);
    }

    @Override
    public RepositoryType getType() {
        return RepositoryType.FileSystem;
    }

    @Override
    public Job lookupJob(String jobName) throws EtlExceptions.InvalidConfigurationException {
        return this.jobDataStore.lookupElement(jobName);
    }

    @Override
    public EtlSource lookupEtlSource(String sourceName) throws EtlExceptions.InvalidConfigurationException {
        return this.etlSourceDataStore.lookupElement(sourceName);
    }

    @Override
    public EtlTarget lookupEtlTarget(String targetName) throws EtlExceptions.InvalidConfigurationException {
        return this.etlTargetDataStore.lookupElement(targetName);
    }

    @Override
    public DataSource lookupDataSource(String dataSourceName) throws EtlExceptions.InvalidConfigurationException {
        return this.dataSourceDataStore.lookupElement(dataSourceName);
    }

    @Override
    public Configuration lookupObject(String objectName) throws EtlExceptions.InvalidConfigurationException {
        return this.jsonDataObjectDataStore.lookupElement(objectName);
    }

    private File getPath(String pathKey, Map<String, String> parameters) {
        String path = parameters.get(pathKey);
        if (path == null || path.isEmpty()) {
            throw new IllegalStateException("could not find - " + pathKey + " in parameters");
        }
        return new File(path);
    }

    private static class DataStore<T> {
        private final File dataFile;
        private final ConfigurationType configurationType;
        private Map<String, T> elements;

        private final Function<T, String> identifier;
        private final Function<Configuration, T> mapper;

        public DataStore(ConfigurationType configurationType, File dataFile, Function<Configuration, T> mapper, Function<T, String> identifier) {
            this.configurationType = configurationType;
            this.dataFile = dataFile;
            this.mapper = mapper;
            this.identifier = identifier;
        }

        public T lookupElement(String key) throws EtlExceptions.InvalidConfigurationException {
            if (this.elements == null) {
                this.readDataFile();
            }
            return this.elements.get(key);
        }

        private void readDataFile() throws EtlExceptions.InvalidConfigurationException {
            Configuration configuration = ConfigurationFactory.parse(this.dataFile, this.configurationType);
            this.elements = configuration.getAsList().stream().map(mapper).collect(Collectors.toMap(identifier, Function.identity()));
        }
    }

    private static class ConfigMapper {

        private static final String[] DATA_SOURCE_REQ_SOURCE_FIELDS = new String[]{EtlConstants.TYPE_FIELD, EtlConstants.ID_FIELD,
                EtlConstants.NAME_FIELD};

        private static final String[] ETL_TARGET_REQ_SOURCE_FIELDS = new String[]{EtlConstants.TYPE_FIELD, EtlConstants.ID_FIELD,
                EtlConstants.NAME_FIELD, EtlConstants.DS_NAME_FIELD, EtlConstants.ETL_TARGET_SAVE_MODE_FIELD};

        private static final String[] ETL_SOURCE_REQ_SOURCE_FIELDS = new String[]{EtlConstants.TYPE_FIELD, EtlConstants.ID_FIELD,
                EtlConstants.NAME_FIELD, EtlConstants.DS_NAME_FIELD, EtlConstants.ETL_SOURCE_ALIAS};

        private static final String[] ETL_JOB_REQ_FIELDS = new String[]{EtlConstants.ID_FIELD, EtlConstants.NAME_FIELD,
                EtlConstants.ETL_JOB_TARGETS_FIELD, EtlConstants.ETL_JOB_STEPS_FIELD};

        private static final String[] ETL_STEP_REQ_FIELDS = new String[]{EtlConstants.ID_FIELD, EtlConstants.NAME_FIELD,
                EtlConstants.ETL_STEP_SOURCES_FIELD, EtlConstants.ETL_STEP_OP_SOURCE_NAME_FIELD,
                EtlConstants.ETL_STEP_OP_SOURCE_ALIAS_FIELD, EtlConstants.ETL_STEP_FUNCTIONS_FIELD,
                EtlConstants.ETL_STEP_INDEX_FIELD, EtlConstants.ETL_STEP_PRIMARY_SOURCE_FIELD};

        private static final String[] ETL_FUNCTION_REQ_FIELDS = new String[]{EtlConstants.TYPE_FIELD, EtlConstants.ID_FIELD,
                EtlConstants.NAME_FIELD,};


        static Job mapJob(Configuration configuration) {
            Preconditions.validateFields(configuration, ETL_JOB_REQ_FIELDS);

            Job.Builder builder = new Job.Builder().withId(new StringId(configuration.getStringValue(EtlConstants.ID_FIELD)))
                    .withName(configuration.getStringValue(EtlConstants.NAME_FIELD))
                    .withDescription(configuration.getStringValue(EtlConstants.DESCRIPTION_FIELD, null))
                    .withActive(configuration.getBooleanValue(EtlConstants.ACTIVE_FIELD, false))
                    .withConfiguration(configuration.getConfiguration(EtlConstants.CONFIGURATION_FIELD, null));

            configuration.getListValue(EtlConstants.ETL_JOB_TARGETS_FIELD).forEach(builder::withTarget);
            configuration.getConfiguration(EtlConstants.ETL_JOB_STEPS_FIELD).getAsList()
                    .stream().map(ConfigMapper::mapStep)
                    .forEach(builder::withStep);

            return builder.build();
        }

        private static Step mapStep(Configuration configuration) {
            Preconditions.validateFields(configuration, ETL_STEP_REQ_FIELDS);
            Step.Builder builder = new Step.Builder()
                    .withId(new StringId(configuration.getStringValue(EtlConstants.ID_FIELD)))
                    .withName(configuration.getStringValue(EtlConstants.NAME_FIELD))
                    .withDescription(configuration.getStringValue(EtlConstants.DESCRIPTION_FIELD, null))
                    .withActive(configuration.getBooleanValue(EtlConstants.ACTIVE_FIELD, false))
                    .withConfiguration(configuration.getConfiguration(EtlConstants.CONFIGURATION_FIELD, null))
                    .withStepIndex(configuration.getIntValue(EtlConstants.ETL_STEP_INDEX_FIELD))
                    .withOutputSourceName(configuration.getStringValue(EtlConstants.ETL_STEP_OP_SOURCE_NAME_FIELD))
                    .withOutputSourceAlias(configuration.getStringValue(EtlConstants.ETL_STEP_OP_SOURCE_ALIAS_FIELD))
                    .withPrimarySource(configuration.getStringValue(EtlConstants.ETL_STEP_PRIMARY_SOURCE_FIELD));

            configuration.getListValue(EtlConstants.ETL_STEP_SOURCES_FIELD).forEach(builder::withSource);
            configuration.getConfiguration(EtlConstants.ETL_STEP_FUNCTIONS_FIELD).getAsList()
                    .stream().map(ConfigMapper::mapEtlFunction)
                    .forEach(builder::withEtlFunction);
            return builder.build();
        }

        private static EtlFunction mapEtlFunction(Configuration configuration) {
            Preconditions.validateFields(configuration, ETL_FUNCTION_REQ_FIELDS);
            String type = configuration.getStringValue(EtlConstants.TYPE_FIELD);

            Id id = new StringId(configuration.getStringValue(EtlConstants.ID_FIELD));
            String name = configuration.getStringValue(EtlConstants.NAME_FIELD);
            String description = configuration.getStringValue(EtlConstants.DESCRIPTION_FIELD, null);
            boolean isActive = configuration.getBooleanValue(EtlConstants.ACTIVE_FIELD, false);

            if (EtlConstants.ETL_RENAME_COLUMN_FUNCTION.equals(type)) {
                Preconditions.validateFields(configuration, EtlConstants.OLD_NAME_FIELD, EtlConstants.NEW_NAME_FIELD);
                return new RenameColumnFunction(id, name, description, isActive,
                        configuration.getStringValue(EtlConstants.OLD_NAME_FIELD),
                        configuration.getStringValue(EtlConstants.NEW_NAME_FIELD));
            } else if (EtlConstants.ETL_ADD_CONSTANT_VALUE_FUNCTION.equals(type)) {
                Preconditions.validateFields(configuration, EtlConstants.COLUMN_NAME_FIELD, EtlConstants.VALUE_FIELD);
                return new AddConstantValueFunction(id, name, description, isActive,
                        configuration.getStringValue(EtlConstants.COLUMN_NAME_FIELD),
                        configuration.getConfiguration(EtlConstants.VALUE_FIELD));
            } else if (EtlConstants.ETL_DROP_COLUMN_FUNCTION.equals(type)) {
                Preconditions.validateFields(configuration, EtlConstants.COLUMN_NAME_FIELD);
                return new DropColumnFunction(id, name, description, isActive,
                        configuration.getStringValue(EtlConstants.COLUMN_NAME_FIELD));
            } else if (EtlConstants.ETL_REPARTITION_DATASET_FUNCTION.equals(type)) {
                Preconditions.validateFields(configuration, EtlConstants.REPARTITION_COLUMNS_FIELD, EtlConstants.NUM_PARTITIONS_FIELD);
                return new RepartitionDatasetFunction(id, name, description, isActive,
                        configuration.getListValue(EtlConstants.REPARTITION_COLUMNS_FIELD),
                        configuration.getIntValue(EtlConstants.NUM_PARTITIONS_FIELD));
            } else if (EtlConstants.ETL_PERSIST_DATASET_FUNCTION.equals(type)) {
                Preconditions.validateFields(configuration, EtlConstants.PERSIST_LEVEL_FIELD);
                return new PersistDatasetFunction(id, name, description, isActive,
                        configuration.getStringValue(EtlConstants.PERSIST_LEVEL_FIELD));
            } else if (EtlConstants.ETL_UN_PERSIST_DATASET_FUNCTION.equals(type)) {
                return new UnPersistDatasetFunction(id, name, description, isActive);
            } else if (EtlConstants.ETL_GROUP_BY_DATASET_FUNCTION.equals(type)) {
                Preconditions.validateFields(configuration, EtlConstants.AGGREGATE_VALUES_FIELD, EtlConstants.GROUP_BY_COLUMNS_FIELD);
                List<AggregateValue> aggregateValues = configuration.getConfiguration(EtlConstants.AGGREGATE_VALUES_FIELD).getAsList()
                        .stream().map(conf -> {
                            String aggType = configuration.getStringValue(EtlConstants.TYPE_FIELD);
                            if (EtlConstants.AGGREGATE_VALUE_MAX_FIELD.equals(aggType)) {
                                Preconditions.validateFields(configuration, EtlConstants.COLUMN_NAME_FIELD, EtlConstants.AGGREGATE_VALUE_RESULT_ALIAS_FIELD);
                                return new MaxValue(conf.getStringValue(EtlConstants.COLUMN_NAME_FIELD),
                                        conf.getStringValue(EtlConstants.AGGREGATE_VALUE_RESULT_ALIAS_FIELD));
                            } else if (EtlConstants.AGGREGATE_VALUE_SUM_FIELD.equals(aggType)) {
                                Preconditions.validateFields(configuration, EtlConstants.COLUMN_NAME_FIELD, EtlConstants.AGGREGATE_VALUE_RESULT_ALIAS_FIELD);
                                return new SumValue(conf.getStringValue(EtlConstants.COLUMN_NAME_FIELD),
                                        conf.getStringValue(EtlConstants.AGGREGATE_VALUE_RESULT_ALIAS_FIELD));
                            } else {
                                throw new IllegalStateException("etl function type not supported - " + type);
                            }
                        }).collect(Collectors.toList());
                return new GroupByDatasetFunction(id, name, description, isActive,
                        configuration.getListValue(EtlConstants.GROUP_BY_COLUMNS_FIELD),
                        aggregateValues);
            } else if (EtlConstants.ETL_DATASET_UNION_FUNCTION.equals(type)) {
                Preconditions.validateFields(configuration, EtlConstants.SECOND_DATASET_NAME_FIELD, EtlConstants.UNION_BY_NAME_FIELD);
                return new DatasetUnionFunction(id, name, description, isActive,
                        configuration.getStringValue(EtlConstants.SECOND_DATASET_NAME_FIELD),
                        configuration.getBooleanValue(EtlConstants.UNION_BY_NAME_FIELD));
            } else if (EtlConstants.ETL_DATASET_REGISTER_AS_TABLE_FUNCTION.equals(type)) {
                Preconditions.validateFields(configuration, EtlConstants.DATASET_NAME_FIELD);
                return new DatasetRegisterAsTableFunction(id, name, description, isActive,
                        configuration.getStringValue(EtlConstants.DATASET_NAME_FIELD));
            } else if (EtlConstants.ETL_FILTER_DATASET_FUNCTION.equals(type)) {
                Preconditions.validateFields(configuration, EtlConstants.FILTER_CONDITION_FIELD);
                return new FilterDatasetFunction(id, name, description, isActive,
                        configuration.getStringValue(EtlConstants.FILTER_CONDITION_FIELD));
            } else if (EtlConstants.ETL_SORT_DATASET_FUNCTION.equals(name)) {
                Preconditions.validateFields(configuration, EtlConstants.SORT_COLUMNS_FIELD);
                Map<String, SortDatasetFunction.SortOrder> sortColumns =
                        configuration.getValueMap(EtlConstants.SORT_COLUMNS_FIELD).entrySet()
                                .stream().collect(Collectors.toMap(Map.Entry::getKey,
                                entry -> SortDatasetFunction.SortOrder.getSortOrder(entry.getValue())));
                return new SortDatasetFunction(id, name, description, isActive, sortColumns);
            } else if (EtlConstants.ETL_CURRENT_DATE_FUNCTION.equals(type)) {
                Preconditions.validateFields(configuration, EtlConstants.COLUMN_NAME_FIELD, EtlConstants.FORMAT_FIELD);
                return new DateAndTimeFunction.CurrentDateFunction(id, name, description, isActive,
                        configuration.getStringValue(EtlConstants.COLUMN_NAME_FIELD),
                        configuration.getStringValue(EtlConstants.FORMAT_FIELD));
            } else if (EtlConstants.ETL_CURRENT_TIMESTAMP_FUNCTION.equals(type)) {
                Preconditions.validateFields(configuration, EtlConstants.COLUMN_NAME_FIELD);
                return new DateAndTimeFunction.CurrentTimestampFunction(id, name, description, isActive,
                        configuration.getStringValue(EtlConstants.COLUMN_NAME_FIELD));
            } else if (EtlConstants.ETL_TO_DATE_FUNCTION.equals(type)) {
                Preconditions.validateFields(configuration, EtlConstants.COLUMN_NAME_FIELD, EtlConstants.FORMAT_FIELD, EtlConstants.SOURCE_COLUMN_FIELD);
                return new DateAndTimeFunction.ToDateFunction(id, name, description, isActive,
                        configuration.getStringValue(EtlConstants.COLUMN_NAME_FIELD),
                        configuration.getStringValue(EtlConstants.FORMAT_FIELD),
                        configuration.getStringValue(EtlConstants.SOURCE_COLUMN_FIELD));
            } else if (EtlConstants.ETL_TO_TIMESTAMP_FUNCTION.equals(type)) {
                Preconditions.validateFields(configuration, EtlConstants.COLUMN_NAME_FIELD, EtlConstants.FORMAT_FIELD, EtlConstants.SOURCE_COLUMN_FIELD);
                return new DateAndTimeFunction.ToTimestampFunction(id, name, description, isActive,
                        configuration.getStringValue(EtlConstants.COLUMN_NAME_FIELD),
                        configuration.getStringValue(EtlConstants.FORMAT_FIELD),
                        configuration.getStringValue(EtlConstants.SOURCE_COLUMN_FIELD));
            } else if (EtlConstants.ETL_SUM_COLUMN_FUNCTION.equals(type)) {
                Preconditions.validateFields(configuration, EtlConstants.RESULT_COLUMN_NAME_FIELD, EtlConstants.COLUMNS_FIELD);
                return new SumColumnFunction(id, name, description, isActive,
                        configuration.getStringValue(EtlConstants.RESULT_COLUMN_NAME_FIELD),
                        configuration.getListValue(EtlConstants.COLUMNS_FIELD));
            } else if (EtlConstants.ETL_CAST_COLUMN_FUNCTION.equals(type)) {
                Preconditions.validateFields(configuration, EtlConstants.RESULT_COLUMN_NAME_FIELD, EtlConstants.SOURCE_COLUMN_FIELD, EtlConstants.TO_TYPE_FIELD);
                return new CastColumnFunction(id, name, description, isActive,
                        configuration.getStringValue(EtlConstants.RESULT_COLUMN_NAME_FIELD),
                        configuration.getStringValue(EtlConstants.SOURCE_COLUMN_FIELD),
                        configuration.getStringValue(EtlConstants.TO_TYPE_FIELD));
            } else if (EtlConstants.ETL_INNER_JOIN_DATASET_FUNCTION.equals(type)) {
                Preconditions.validateFields(configuration, EtlConstants.LEFT_DATASET_NAME_FIELD, EtlConstants.RIGHT_DATASET_NAME, EtlConstants.LEFT_DATASET_COLUMN_NAME_FIELD,
                        EtlConstants.RIGHT_DATASET_COLUMN_NAME_FIELD);
                return new InnerJoinDatasetFunction(id, name, description, isActive,
                        configuration.getStringValue(EtlConstants.LEFT_DATASET_NAME_FIELD),
                        configuration.getStringValue(EtlConstants.RIGHT_DATASET_NAME),
                        configuration.getStringValue(EtlConstants.LEFT_DATASET_COLUMN_NAME_FIELD),
                        configuration.getStringValue(EtlConstants.RIGHT_DATASET_COLUMN_NAME_FIELD));
            } else if (EtlConstants.ETL_LEFT_JOIN_DATASET_FUNCTION.equals(type)) {
                Preconditions.validateFields(configuration, EtlConstants.LEFT_DATASET_NAME_FIELD, EtlConstants.RIGHT_DATASET_NAME, EtlConstants.LEFT_DATASET_COLUMN_NAME_FIELD,
                        EtlConstants.RIGHT_DATASET_COLUMN_NAME_FIELD);
                return new LeftJoinDatasetFunction(id, name, description, isActive,
                        configuration.getStringValue(EtlConstants.LEFT_DATASET_NAME_FIELD),
                        configuration.getStringValue(EtlConstants.RIGHT_DATASET_NAME),
                        configuration.getStringValue(EtlConstants.LEFT_DATASET_COLUMN_NAME_FIELD),
                        configuration.getStringValue(EtlConstants.RIGHT_DATASET_COLUMN_NAME_FIELD));
            } else if (EtlConstants.ETL_RIGHT_JOIN_DATASET_FUNCTION.equals(type)) {
                Preconditions.validateFields(configuration, EtlConstants.LEFT_DATASET_NAME_FIELD, EtlConstants.RIGHT_DATASET_NAME, EtlConstants.LEFT_DATASET_COLUMN_NAME_FIELD,
                        EtlConstants.RIGHT_DATASET_COLUMN_NAME_FIELD);
                return new RightJoinDatasetFunction(id, name, description, isActive,
                        configuration.getStringValue(EtlConstants.LEFT_DATASET_NAME_FIELD),
                        configuration.getStringValue(EtlConstants.RIGHT_DATASET_NAME),
                        configuration.getStringValue(EtlConstants.LEFT_DATASET_COLUMN_NAME_FIELD),
                        configuration.getStringValue(EtlConstants.RIGHT_DATASET_COLUMN_NAME_FIELD));
            } else if (EtlConstants.ETL_SQL_DATASET_FUNCTION.equals(type)) {
                Preconditions.validateFields(configuration, EtlConstants.QUERY_TYPE_FIELD, EtlConstants.QUERY_SOURCE_FIELD);
                return new SQLFunction(id, name, description, isActive,
                        configuration.getStringValue(EtlConstants.QUERY_TYPE_FIELD),
                        configuration.getStringValue(EtlConstants.QUERY_SOURCE_FIELD));
            } else {
                throw new IllegalStateException("etl function type not supported - " + type);
            }
        }

        static EtlSource mapEtlSource(Configuration configuration) {
            Preconditions.validateFields(configuration, ETL_SOURCE_REQ_SOURCE_FIELDS);
            String type = configuration.getStringValue(EtlConstants.TYPE_FIELD);

            if (EtlConstants.ETL_SOURCE_TYPE_FILE_SYSTEM.equals(type)) {
                Preconditions.validateFields(configuration, EtlConstants.ETL_SOURCE_FILE_FORMAT_FIELD, EtlConstants.ETL_SOURCE_LOCATION_NAME_FIELD);
                FileEtlSource.Builder builder = new FileEtlSource.Builder()
                        .withId(new StringId(configuration.getStringValue(EtlConstants.ID_FIELD)))
                        .withName(configuration.getStringValue(EtlConstants.NAME_FIELD))
                        .withDescription(configuration.getStringValue(EtlConstants.DESCRIPTION_FIELD, null))
                        .withActive(configuration.getBooleanValue(EtlConstants.ACTIVE_FIELD, false))
                        .withDataSourceName(configuration.getStringValue(EtlConstants.DS_NAME_FIELD))
                        .withAlias(configuration.getStringValue(EtlConstants.ETL_SOURCE_ALIAS))
                        .withFileFormat(configuration.getStringValue(EtlConstants.ETL_SOURCE_FILE_FORMAT_FIELD))
                        .withLocationName(configuration.getStringValue(EtlConstants.ETL_SOURCE_LOCATION_NAME_FIELD))
                        .withConfiguration(configuration.getConfiguration(EtlConstants.CONFIGURATION_FIELD, null));
                return builder.build();
            } else {
                throw new IllegalStateException("source type not supported - " + type);
            }
        }

        static EtlTarget mapEtlTarget(Configuration configuration) {
            Preconditions.validateFields(configuration, ETL_TARGET_REQ_SOURCE_FIELDS);
            String type = configuration.getStringValue(EtlConstants.TYPE_FIELD);

            if (EtlConstants.ETL_TARGET_TYPE_FILE_SYSTEM.equals(type)) {
                Preconditions.validateFields(configuration, EtlConstants.ETL_TARGET_FORMAT_FIELD, EtlConstants.ETL_TARGET_LOCATION_NAME_FIELD);
                FileEtlTarget.Builder builder = new FileEtlTarget.Builder()
                        .withId(new StringId(configuration.getStringValue(EtlConstants.ID_FIELD)))
                        .withName(configuration.getStringValue(EtlConstants.NAME_FIELD))
                        .withDescription(configuration.getStringValue(EtlConstants.DESCRIPTION_FIELD, null))
                        .withActive(configuration.getBooleanValue(EtlConstants.ACTIVE_FIELD, false))
                        .withSaveMode(configuration.getStringValue(EtlConstants.ETL_TARGET_SAVE_MODE_FIELD))
                        .withDataSourceName(configuration.getStringValue(EtlConstants.DS_NAME_FIELD))
                        .withFormat(configuration.getStringValue(EtlConstants.ETL_TARGET_FORMAT_FIELD))
                        .withLocationName(configuration.getStringValue(EtlConstants.ETL_TARGET_LOCATION_NAME_FIELD))
                        .withConfiguration(configuration.getConfiguration(EtlConstants.CONFIGURATION_FIELD, null));
                return builder.build();
            } else {
                throw new IllegalStateException("target type not supported - " + type);
            }
        }

        static DataSource mapDataSource(Configuration configuration) {
            Preconditions.validateFields(configuration, DATA_SOURCE_REQ_SOURCE_FIELDS);

            String type = configuration.getStringValue(EtlConstants.TYPE_FIELD);

            if (EtlConstants.DS_TYPE_FILE_SYSTEM.equals(type)) {
                Preconditions.validateFields(configuration, EtlConstants.NAMED_PATH_FIELD);
                FileSystemDataSource.Builder builder = new FileSystemDataSource.Builder()
                        .withId(new StringId(configuration.getStringValue(EtlConstants.ID_FIELD)))
                        .withName(configuration.getStringValue(EtlConstants.NAME_FIELD))
                        .withDescription(configuration.getStringValue(EtlConstants.DESCRIPTION_FIELD, null))
                        .withActive(configuration.getBooleanValue(EtlConstants.ACTIVE_FIELD, false))
                        .withNamedPaths(configuration.getValueMap(EtlConstants.NAMED_PATH_FIELD))
                        .withConfiguration(configuration.getConfiguration(EtlConstants.CONFIGURATION_FIELD, null));
                return builder.build();
            } else {
                throw new IllegalStateException("data source type not supported - " + type);
            }
        }

    }
}
