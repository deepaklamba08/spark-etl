package org.sp.etl.core.repo.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import org.sp.etl.common.ds.DataSource;
import org.sp.etl.common.io.source.EtlSource;
import org.sp.etl.common.io.tr.EtlTarget;
import org.sp.etl.common.model.job.Job;
import org.sp.etl.common.repo.EtlRepositroty;
import org.sp.etl.common.repo.RepositrotyType;
import org.sp.etl.common.util.JsonDataObject;
import org.sp.etl.common.util.JsonDataUtils;

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

    private File jobConfFile;
    private File dbConfFile;
    private File sourceConfFile;
    private File targetConfFile;
    private File objectConfFile;

    private Map<String, Job> jobs;
    private Map<String, JsonDataObject> objectConfigs;
    private Map<String, EtlSource> sources;
    private Map<String, DataSource> dataSources;
    private Map<String, EtlTarget> targets;

    public LocalFsEtlRepositroty(Map<String, String> parameters) {
        this.jobConfFile = new File(parameters.get(JOB_CONF_FILE_KEY));
        this.dbConfFile = new File(parameters.get(DB_CONF_FILE_KEY));
        this.sourceConfFile = new File(parameters.get(SOURCE_CONF_FILE_KEY));
        this.targetConfFile = new File(parameters.get(TARGET_CONF_FILE_KEY));
        this.objectConfFile = new File(parameters.get(OBJECT_CONF_FILE_KEY));
    }

    @Override
    public RepositrotyType getType() {
        return RepositrotyType.LocalFileSystem;
    }

    @Override
    public Job lookupJob(String jobName) {
        if (this.jobs == null) {
            this.jobs = this.readDataFile(this.jobConfFile, new TypeReference<List<Job>>() {
            }).stream().collect(Collectors.toMap(k -> k.getJobName(), Function.identity()));
        }
        return this.jobs.get(jobName);
    }

    @Override
    public EtlSource lookupEtlSource(String sourceName) {
        if (this.sources == null) {
            this.sources = this.readDataFile(this.sourceConfFile, new TypeReference<List<EtlSource>>() {
            }).stream().collect(Collectors.toMap(k -> k.sourceName(), Function.identity()));
        }
        return this.sources.get(sourceName);
    }

    @Override
    public EtlTarget lookupEtlTarget(String targetName) {
        if (this.targets == null) {
            this.targets = this.readDataFile(this.targetConfFile, new TypeReference<List<EtlTarget>>() {
            }).stream().collect(Collectors.toMap(k -> k.targetName(), Function.identity()));
        }
        return this.targets.get(targetName);
    }

    @Override
    public DataSource lookupDataSource(String dataSourceName) {
        if (this.dataSources == null) {
            this.dataSources = this.readDataFile(this.dbConfFile, new TypeReference<List<DataSource>>() {
            }).stream().collect(Collectors.toMap(k -> k.dataSourceName(), Function.identity()));
        }
        return this.dataSources.get(dataSourceName);
    }

    @Override
    public JsonDataObject lookupObject(String objectName) {
        if (this.objectConfigs == null) {
            this.objectConfigs = this.readDataFile(this.objectConfFile, new TypeReference<List<JsonDataObject>>() {
            }).stream().collect(Collectors.toMap(k -> k.getStringValue(OBJECT_NAME_KEY), Function.identity()));
        }
        return this.objectConfigs.get(objectName);
    }

    private <T> T readDataFile(File fileName, TypeReference<T> typeReference) {
        try {
            return JsonDataUtils.getObjectMapper().readValue(fileName, typeReference);
        } catch (IOException e) {
            throw new IllegalStateException("error occurred while reading data file", e);
        }
    }
}
