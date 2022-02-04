package org.sp.etl.core.repo.impl;

import org.sp.etl.common.ds.DataSource;
import org.sp.etl.common.ds.FileSystemDataSource;
import org.sp.etl.common.exception.EtlExceptions;
import org.sp.etl.common.io.source.EtlSource;
import org.sp.etl.common.io.source.impl.FileEtlSource;
import org.sp.etl.common.io.tr.EtlTarget;
import org.sp.etl.common.io.tr.impl.FileEtlTarget;
import org.sp.etl.common.model.Configuration;
import org.sp.etl.common.model.ConfigurationType;
import org.sp.etl.common.model.StringId;
import org.sp.etl.common.model.job.Job;
import org.sp.etl.common.model.step.Step;
import org.sp.etl.common.repo.EtlRepository;
import org.sp.etl.common.repo.RepositoryType;
import org.sp.etl.common.util.ConfigurationFactory;
import org.sp.etl.common.util.EtlConstants;
import org.sp.etl.common.util.Preconditions;
import function.EtlFunction;


import java.io.File;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FsEtlRepository implements EtlRepository {

    private DataStore<Job> jobDataStore;
    private DataStore<Configuration> jsonDataObjectDataStore;
    private DataStore<EtlSource> etlSourceDataStore;
    private DataStore<DataSource> dataSourceDataStore;
    private DataStore<EtlTarget> etlTargetDataStore;

    public FsEtlRepository(Map<String, String> parameters) {
        this.jobDataStore = new DataStore<>(this.getPath(EtlConstants.JOB_CONF_FILE_KEY, parameters), ConfigMapper::mapJob, job -> job.getName());
        this.jsonDataObjectDataStore = new DataStore<>(this.getPath(EtlConstants.OBJECT_CONF_FILE_KEY, parameters), Function.identity(), jo -> jo.getName());
        this.etlSourceDataStore = new DataStore<>(this.getPath(EtlConstants.SOURCE_CONF_FILE_KEY, parameters), ConfigMapper::mapEtlSource, s -> s.getName());
        this.dataSourceDataStore = new DataStore<>(this.getPath(EtlConstants.DB_CONF_FILE_KEY, parameters), ConfigMapper::mapDataSource, d -> d.getName());
        this.etlTargetDataStore = new DataStore<>(this.getPath(EtlConstants.TARGET_CONF_FILE_KEY, parameters), ConfigMapper::mapEtlTarget, t -> t.getName());
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

    private class DataStore<T> {
        private File dataFile;
        private ConfigurationType configurationType;
        private Map<String, T> elements;

        private Function<T, String> identifier;
        private Function<Configuration, T> mapper;

        public DataStore(File dataFile, Function<Configuration, T> mapper, Function<T, String> identifier) {
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

        private static String[] DATA_SOURCE_REQ_SOURCE_FIELDS = new String[]{EtlConstants.TYPE_FIELD, EtlConstants.ID_FIELD,
                EtlConstants.NAME_FIELD, EtlConstants.NAMED_PATH_FIELD};

        private static String[] ETL_TARGET_REQ_SOURCE_FIELDS = new String[]{EtlConstants.TYPE_FIELD, EtlConstants.ID_FIELD,
                EtlConstants.NAME_FIELD, EtlConstants.DS_NAME_FIELD, EtlConstants.ETL_TARGET_SAVE_MODE_FIELD};

        private static String[] ETL_SOURCE_REQ_SOURCE_FIELDS = new String[]{EtlConstants.TYPE_FIELD, EtlConstants.ID_FIELD,
                EtlConstants.NAME_FIELD, EtlConstants.DS_NAME_FIELD, EtlConstants.ETL_SOURCE_ALIAS};

        private static String[] ETL_JOB_REQ_FIELDS = new String[]{EtlConstants.ID_FIELD, EtlConstants.NAME_FIELD,
                EtlConstants.ETL_JOB_TARGETS_FIELD, EtlConstants.ETL_JOB_STEPS_FIELD};

        private static String[] ETL_STEP_REQ_FIELDS = new String[]{EtlConstants.ID_FIELD, EtlConstants.NAME_FIELD,
                EtlConstants.ETL_STEP_SOURCES_FIELD, EtlConstants.ETL_STEP_OP_SOURCE_NAME_FIELD,
                EtlConstants.ETL_STEP_OP_SOURCE_ALIAS_FIELD, EtlConstants.ETL_STEP_FUNCTIONS_FIELD,
                EtlConstants.ETL_STEP_INDEX_FIELD};

        private static String[] ETL_FUNCTION_REQ_FIELDS = new String[]{EtlConstants.TYPE_FIELD, EtlConstants.ID_FIELD,
                EtlConstants.NAME_FIELD, EtlConstants.NAMED_PATH_FIELD};


        static Job mapJob(Configuration configuration) {
            Preconditions.validateFields(configuration, "not all mandatory fields are present", ETL_JOB_REQ_FIELDS);

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
            Preconditions.validateFields(configuration, "not all mandatory fields are present", ETL_STEP_REQ_FIELDS);
            Step.Builder builder = new Step.Builder()
                    .withId(new StringId(configuration.getStringValue(EtlConstants.ID_FIELD)))
                    .withName(configuration.getStringValue(EtlConstants.NAME_FIELD))
                    .withDescription(configuration.getStringValue(EtlConstants.DESCRIPTION_FIELD, null))
                    .withActive(configuration.getBooleanValue(EtlConstants.ACTIVE_FIELD, false))
                    .withConfiguration(configuration.getConfiguration(EtlConstants.CONFIGURATION_FIELD, null))
                    .withStepIndex(configuration.getIntValue(EtlConstants.ETL_STEP_INDEX_FIELD))
                    .withOutputSourceName(configuration.getStringValue(EtlConstants.ETL_STEP_OP_SOURCE_NAME_FIELD))
                    .withOutputSourceAlias(configuration.getStringValue(EtlConstants.ETL_STEP_OP_SOURCE_ALIAS_FIELD));


            configuration.getConfiguration(EtlConstants.ETL_STEP_FUNCTIONS_FIELD).getAsList()
                    .stream().map(ConfigMapper::mapEtlFunction)
                    .forEach(builder::withEtlFunction);
            return builder.build();
        }

        private static EtlFunction mapEtlFunction(Configuration configuration) {
            Preconditions.validateFields(configuration, "not all mandatory fields are present", ETL_FUNCTION_REQ_FIELDS);
            String type = configuration.getStringValue(EtlConstants.TYPE_FIELD);

            return null;
        }

        static EtlSource mapEtlSource(Configuration configuration) {
            Preconditions.validateFields(configuration, "not all mandatory fields are present", ETL_SOURCE_REQ_SOURCE_FIELDS);
            String type = configuration.getStringValue(EtlConstants.TYPE_FIELD);
            EtlSource source = null;
            if (EtlConstants.ETL_SOURCE_TYPE_FILE_SYSTEM.equals(type)) {
                FileEtlSource.Builder builder = new FileEtlSource.Builder()
                        .withId(new StringId(configuration.getStringValue(EtlConstants.ID_FIELD)))
                        .withName(configuration.getStringValue(EtlConstants.NAME_FIELD))
                        .withDescription(configuration.getStringValue(EtlConstants.DESCRIPTION_FIELD, null))
                        .withActive(configuration.getBooleanValue(EtlConstants.ACTIVE_FIELD, false))
                        .withDataSourceName(configuration.getStringValue(EtlConstants.DS_NAME_FIELD))
                        .withAlias(configuration.getStringValue(EtlConstants.ETL_SOURCE_ALIAS))
                        .withConfiguration(configuration.getConfiguration(EtlConstants.CONFIGURATION_FIELD, null));
                source = builder.build();
            } else {
                throw new IllegalStateException("source type not supported - " + type);
            }
            return source;
        }

        static EtlTarget mapEtlTarget(Configuration configuration) {
            Preconditions.validateFields(configuration, "not all mandatory fields are present", ETL_TARGET_REQ_SOURCE_FIELDS);
            String type = configuration.getStringValue(EtlConstants.TYPE_FIELD);
            EtlTarget target = null;
            if (EtlConstants.ETL_TARGET_TYPE_FILE_SYSTEM.equals(type)) {
                FileEtlTarget.Builder builder = new FileEtlTarget.Builder()
                        .withId(new StringId(configuration.getStringValue(EtlConstants.ID_FIELD)))
                        .withName(configuration.getStringValue(EtlConstants.NAME_FIELD))
                        .withDescription(configuration.getStringValue(EtlConstants.DESCRIPTION_FIELD, null))
                        .withActive(configuration.getBooleanValue(EtlConstants.ACTIVE_FIELD, false))
                        .withSaveMode(configuration.getStringValue(EtlConstants.ETL_TARGET_SAVE_MODE_FIELD))
                        .withDataSourceName(configuration.getStringValue(EtlConstants.DS_NAME_FIELD))
                        .withConfiguration(configuration.getConfiguration(EtlConstants.CONFIGURATION_FIELD, null));
                target = builder.build();
            } else {
                throw new IllegalStateException("target type not supported - " + type);
            }
            return target;
        }

        static DataSource mapDataSource(Configuration configuration) {
            Preconditions.validateFields(configuration, "not all mandatory fields are present", DATA_SOURCE_REQ_SOURCE_FIELDS);

            String type = configuration.getStringValue(EtlConstants.TYPE_FIELD);
            DataSource dataSource = null;
            if (EtlConstants.DS_TYPE_FILE_SYSTEM.equals(type)) {
                FileSystemDataSource.Builder builder = new FileSystemDataSource.Builder()
                        .withId(new StringId(configuration.getStringValue(EtlConstants.ID_FIELD)))
                        .withName(configuration.getStringValue(EtlConstants.NAME_FIELD))
                        .withDescription(configuration.getStringValue(EtlConstants.DESCRIPTION_FIELD, null))
                        .withActive(configuration.getBooleanValue(EtlConstants.ACTIVE_FIELD, false))
                        .withNamedPaths(configuration.getValueMap(EtlConstants.NAMED_PATH_FIELD))
                        .withConfiguration(configuration.getConfiguration(EtlConstants.CONFIGURATION_FIELD, null));
                dataSource = builder.build();
            } else {
                throw new IllegalStateException("data source type not supported - " + type);
            }
            return dataSource;
        }

    }
}
