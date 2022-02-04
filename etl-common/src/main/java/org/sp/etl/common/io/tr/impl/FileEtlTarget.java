package org.sp.etl.common.io.tr.impl;

import org.sp.etl.common.io.tr.EtlTarget;
import org.sp.etl.common.model.Configuration;
import org.sp.etl.common.model.Id;

import java.util.Map;

public class FileEtlTarget implements EtlTarget {

    private Id id;
    private String name;
    private String description;
    private boolean isActive;
    private String dataSourceName;
    private String locationName;
    private String saveMode;
    private String format;
    private Map<String, String> writerConfig;
    private Configuration configuration;

    protected FileEtlTarget(Id id, String name, String description, boolean isActive, String dataSourceName, String locationName, String saveMode, String format, Configuration configuration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.isActive = isActive;
        this.dataSourceName = dataSourceName;
        this.locationName = locationName;
        this.saveMode = saveMode;
        this.format = format;
        this.configuration = configuration;
    }

    public String getSaveMode() {
        return saveMode;
    }

    public String getFormat() {
        return format;
    }

    public Map<String, String> getWriterConfig() {
        return writerConfig;
    }

    public String getLocationName() {
        return locationName;
    }

    @Override
    public String dataSourceName() {
        return this.dataSourceName;
    }

    @Override
    public boolean isActive() {
        return this.isActive;
    }

    @Override
    public Id getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    public static class Builder {
        private Id id;
        private String name;
        private String description;
        private boolean isActive;
        private String dataSourceName;
        private String locationName;
        private String saveMode;
        private String format;
        private Configuration configuration;

        public Builder withId(Id id) {
            this.id = id;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder withActive(boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public Builder makeActive() {
            this.isActive = true;
            return this;
        }

        public Builder makeInActive() {
            this.isActive = false;
            return this;
        }

        public Builder withDataSourceName(String dataSourceName) {
            this.dataSourceName = dataSourceName;
            return this;
        }

        public Builder withLocationName(String locationName) {
            this.locationName = locationName;
            return this;
        }

        public Builder withSaveMode(String saveMode) {
            this.saveMode = saveMode;
            return this;
        }

        public Builder withFormat(String format) {
            this.format = format;
            return this;
        }

        public Builder withConfiguration(Configuration configuration) {
            this.configuration = configuration;
            return this;
        }

        public FileEtlTarget build() {
            return new FileEtlTarget(this.id, this.name, this.description, this.isActive, this.dataSourceName,this.locationName, this.saveMode, this.format, this.configuration);
        }
    }
}
