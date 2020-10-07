package org.sp.etl.common.io.tr.impl;

import org.sp.etl.common.io.tr.EtlTarget;

import java.util.Map;

public abstract class FileEtlTarget implements EtlTarget {

    private String name;
    private String dataSourceName;
    private String fileName;
    private String saveMode;
    private Map<String, String> config;

    @Override
    public Map<String, String> config() {
        return this.config;
    }

    public abstract String fileFormat();

    @Override
    public String dataSourceName() {
        return this.dataSourceName;
    }

    @Override
    public String targetName() {
        return this.name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    public String getDataSourceName() {
        return dataSourceName;
    }

    public Map<String, String> getConfig() {
        return config;
    }

    public void setConfig(Map<String, String> config) {
        this.config = config;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setSaveMode(String saveMode) {
        this.saveMode = saveMode;
    }

    public String getSaveMode() {
        return saveMode;
    }
}
