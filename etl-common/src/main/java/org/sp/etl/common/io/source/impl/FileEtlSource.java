package org.sp.etl.common.io.source.impl;

import org.sp.etl.common.io.source.EtlSource;
import org.sp.etl.common.model.Configuration;
import org.sp.etl.common.model.Id;
import org.sp.etl.common.util.EtlConstants;

import java.util.Map;
import java.util.Optional;

public class FileEtlSource implements EtlSource {

    private Id id;
    private String name;
    private String description;
    private boolean isActive;
    private String dataSourceName;
    private String alias;
    private String fileFormat;
    private String locationName;
    private Configuration configuration;


    public FileEtlSource(Id id, String name, String description, boolean isActive, String dataSourceName, String alias, String fileFormat, String locationName, Configuration configuration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.isActive = isActive;
        this.dataSourceName = dataSourceName;
        this.alias = alias;
        this.fileFormat = fileFormat;
        this.locationName = locationName;
        this.configuration = configuration;
    }


    @Override
    public String getAlias() {
        return alias;
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

    public String getFileFormat() {
        return fileFormat;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public String getLocationName() {
        return locationName;
    }

    public Optional<Map<String, String>> readerConfig() {
        return configuration != null ? Optional.of(configuration.getValueMap(EtlConstants.READER_CONFIG)) : Optional.empty();
    }

    public static class Builder {
        private Id id;
        private String name;
        private String description;
        private boolean isActive;
        private String dataSourceName;
        private String alias;
        private String fileFormat;
        private String locationName;
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

        public Builder withAlias(String alias) {
            this.alias = alias;
            return this;
        }

        public Builder withFileFormat(String fileFormat) {
            this.fileFormat = fileFormat;
            return this;
        }

        public Builder withLocationName(String locationName) {
            this.locationName = locationName;
            return this;
        }

        public Builder withConfiguration(Configuration configuration) {
            this.configuration = configuration;
            return this;
        }

        public FileEtlSource build() {
            return new FileEtlSource(this.id, this.name, this.description, this.isActive, this.dataSourceName, this.alias, this.fileFormat, this.locationName, this.configuration);
        }
    }

}
