package org.sp.etl.common.io.source.impl;

import org.sp.etl.common.io.source.EtlSource;

import java.util.Map;

public abstract class FileEtlSource implements EtlSource {
    private String name;
    private String dataSourceName;
    private String alias;
    private String fileName;
    private Map<String, String> config;

    @Override
    public String dataSourceName() {
        return dataSourceName;
    }

    @Override
    public String sourceName() {
        return this.name;
    }

    @Override
    public String sourceAlias() {
        return this.alias;
    }

    @Override
    public Map<String, String> config() {
        return this.config;
    }

    public abstract String fileFormat();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDataSourceName() {
        return dataSourceName;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Map<String, String> getConfig() {
        return config;
    }

    public void setConfig(Map<String, String> config) {
        this.config = config;
    }


}
