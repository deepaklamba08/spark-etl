package org.sp.etl.core.repo.impl;

import org.sp.etl.common.ds.DataSource;
import org.sp.etl.common.exception.EtlExceptions;
import org.sp.etl.common.io.source.EtlSource;
import org.sp.etl.common.io.tr.EtlTarget;
import org.sp.etl.common.model.Configuration;
import org.sp.etl.common.model.ConfigurationType;
import org.sp.etl.common.model.job.Job;
import org.sp.etl.common.repo.EtlRepository;
import org.sp.etl.common.repo.RepositoryType;
import org.sp.etl.common.util.ConfigurationFactory;
import org.sp.etl.common.util.EtlConstants;


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
        this.jobDataStore = new DataStore<>(this.getPath(EtlConstants.JOB_CONF_FILE_KEY, parameters), ConfigMapper::mapJob, job -> job.getJobName());
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

        static Job mapJob(Configuration configuration) {

            return null;
        }

        static EtlSource mapEtlSource(Configuration configuration) {

            return null;
        }

        static EtlTarget mapEtlTarget(Configuration configuration) {

            return null;
        }

        static DataSource mapDataSource(Configuration configuration) {

            return null;
        }

    }
}
