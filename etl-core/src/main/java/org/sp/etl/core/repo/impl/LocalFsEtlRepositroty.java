package org.sp.etl.core.repo.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import org.sp.etl.common.ds.DataSource;
import org.sp.etl.common.io.source.EtlSource;
import org.sp.etl.common.io.tr.EtlTarget;
import org.sp.etl.common.model.job.Job;
import org.sp.etl.common.repo.EtlRepositroty;
import org.sp.etl.common.repo.RepositrotyType;
import org.sp.etl.common.model.JsonConfiguration;
import org.sp.etl.common.util.DataUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class LocalFsEtlRepositroty implements EtlRepositroty {

    private static final String OBJECT_NAME_KEY = "objectName";
    private static final String JOB_CONF_FILE_KEY = "jobConfigFile";
    private static final String DB_CONF_FILE_KEY = "dbConfigFile";
    private static final String SOURCE_CONF_FILE_KEY = "sourceConfigFile";
    private static final String TARGET_CONF_FILE_KEY = "targetConfigFile";
    private static final String OBJECT_CONF_FILE_KEY = "objectConfigFile";

    private DataStore<Job> jobDataStore;
    private DataStore<JsonConfiguration> jsonDataObjectDataStore;
    private DataStore<EtlSource> etlSourceDataStore;
    private DataStore<DataSource> dataSourceDataStore;
    private DataStore<EtlTarget> etlTargetDataStore;

    public LocalFsEtlRepositroty(Map<String, String> parameters) {
        this.jobDataStore = new DataStore<>(new File(parameters.get(JOB_CONF_FILE_KEY)), new TypeReference<List<Job>>() {
        }, job -> job.getJobName());
        this.jsonDataObjectDataStore = new DataStore<>(new File(parameters.get(OBJECT_CONF_FILE_KEY)), new TypeReference<List<JsonConfiguration>>() {
        }, jo -> jo.getStringValue(OBJECT_NAME_KEY));
        this.etlSourceDataStore = new DataStore<>(new File(parameters.get(SOURCE_CONF_FILE_KEY)), new TypeReference<List<EtlSource>>() {
        }, s -> s.sourceName());
        this.dataSourceDataStore = new DataStore<>(new File(parameters.get(DB_CONF_FILE_KEY)), new TypeReference<List<DataSource>>() {
        }, d -> d.dataSourceName());
        this.etlTargetDataStore = new DataStore<>(new File(parameters.get(TARGET_CONF_FILE_KEY)), new TypeReference<List<EtlTarget>>() {
        }, t -> t.targetName());
    }

    @Override
    public RepositrotyType getType() {
        return RepositrotyType.       ;
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
    public JsonConfiguration lookupObject(String objectName) {
        return this.jsonDataObjectDataStore.lookupElement(objectName);
    }

    private class DataStore<T> {
        private File dataFile;
        private Map<String, T> elements;

        private Function<T, String> identifier;
        private TypeReference<List<T>> typeReference;

        public DataStore(File dataFile, TypeReference<List<T>> typeReference, Function<T, String> identifier) {
            this.dataFile = dataFile;
            this.typeReference = typeReference;
            this.identifier = identifier;
        }

        public T lookupElement(String jobName) {
            if (this.elements == null) {
                this.elements = this.readDataFile(this.dataFile, this.typeReference)
                        .stream().collect(Collectors.toMap(k -> this.identifier.apply(k), Function.identity()));
            }
            return this.elements.get(jobName);
        }

        private <T> T readDataFile(File fileName, TypeReference<T> typeReference) {
            try {
                return DataUtils.getObjectMapper().readValue(fileName, typeReference);
            } catch (IOException e) {
                throw new IllegalStateException("error occurred while reading data file", e);
            }
        }
    }
}
