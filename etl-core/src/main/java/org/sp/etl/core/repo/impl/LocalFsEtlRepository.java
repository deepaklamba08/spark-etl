package org.sp.etl.core.repo.impl;

import org.sp.etl.common.ds.DataSource;
import org.sp.etl.common.io.source.EtlSource;
import org.sp.etl.common.io.tr.EtlTarget;
import org.sp.etl.common.model.Configuration;
import org.sp.etl.common.model.job.Job;
import org.sp.etl.common.repo.EtlRepository;
import org.sp.etl.common.repo.RepositoryType;


import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class LocalFsEtlRepository implements EtlRepository {

    private static final String OBJECT_NAME_KEY = "objectName";
    private static final String JOB_CONF_FILE_KEY = "jobConfigFile";
    private static final String DB_CONF_FILE_KEY = "dbConfigFile";
    private static final String SOURCE_CONF_FILE_KEY = "sourceConfigFile";
    private static final String TARGET_CONF_FILE_KEY = "targetConfigFile";
    private static final String OBJECT_CONF_FILE_KEY = "objectConfigFile";

    private DataStore<Job> jobDataStore;
    private DataStore<Configuration> jsonDataObjectDataStore;
    private DataStore<EtlSource> etlSourceDataStore;
    private DataStore<DataSource> dataSourceDataStore;
    private DataStore<EtlTarget> etlTargetDataStore;

    public LocalFsEtlRepository(Map<String, String> parameters) {
        this.jobDataStore = new DataStore<>(new File(parameters.get(JOB_CONF_FILE_KEY)), null, job -> job.getJobName());
        this.jsonDataObjectDataStore = new DataStore<>(new File(parameters.get(OBJECT_CONF_FILE_KEY)), null, jo -> jo.getName());
        this.etlSourceDataStore = new DataStore<>(new File(parameters.get(SOURCE_CONF_FILE_KEY)), null, s -> s.getName());
        this.dataSourceDataStore = new DataStore<>(new File(parameters.get(DB_CONF_FILE_KEY)), null, d -> d.getName());
        this.etlTargetDataStore = new DataStore<>(new File(parameters.get(TARGET_CONF_FILE_KEY)), null, t -> t.getName());
    }

    @Override
    public RepositoryType getType() {
        return RepositoryType.FileSystem;
    }

    @Override
    public Job lookupJob(String jobName) {
        return this.jobDataStore.lookupElement(jobName);
    }

    @Override
    public EtlSource lookupEtlSource(String sourceName) {
        return this.etlSourceDataStore.lookupElement(sourceName);
    }

    @Override
    public EtlTarget lookupEtlTarget(String targetName) {
        return this.etlTargetDataStore.lookupElement(targetName);
    }

    @Override
    public DataSource lookupDataSource(String dataSourceName) {
        return this.dataSourceDataStore.lookupElement(dataSourceName);
    }

    @Override
    public Configuration lookupObject(String objectName) {
        return this.jsonDataObjectDataStore.lookupElement(objectName);
    }

    private class DataStore<T> {
        private File dataFile;
        private Map<String, T> elements;

        private Function<T, String> identifier;
        private Function<Configuration, T> mapper;

        public DataStore(File dataFile, Function<Configuration, T> mapper, Function<T, String> identifier) {
            this.dataFile = dataFile;
            this.mapper = mapper;
            this.identifier = identifier;
        }

        public T lookupElement(String key) {
            if (this.elements == null) {
                List<T> items = this.readDataFile(this.dataFile);
                this.elements = items.stream().collect(Collectors.toMap(k -> this.identifier.apply(k), Function.identity()));
            }
            return this.elements.get(key);
        }

        private <T> List<T> readDataFile(File fileName) {
            //          try {
//                DataUtils.getObjectMapper();//.readValue(fileName, null);
            return null;
            //        } catch (IOException e) {
            //           throw new IllegalStateException("error occurred while reading data file", e);
            //     }
        }
    }
}
