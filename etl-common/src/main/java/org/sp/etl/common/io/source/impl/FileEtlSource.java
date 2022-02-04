package org.sp.etl.common.io.source.impl;

import org.sp.etl.common.io.source.EtlSource;
import org.sp.etl.common.model.Configuration;
import org.sp.etl.common.model.Id;

public class FileEtlSource implements EtlSource {

    private Id id;
    private String name;
    private String description;
    private boolean isActive;
    private String dataSourceName;
    private String alias;
    private Configuration configuration;


    public FileEtlSource(Id id, String name, String description, boolean isActive, String dataSourceName, String alias, Configuration configuration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.isActive = isActive;
        this.dataSourceName = dataSourceName;
        this.alias = alias;
        this.configuration = configuration;
    }


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

    public static class Builder {
        private Id id;
        private String name;
        private String description;
        private boolean isActive;
        private String dataSourceName;
        private String alias;
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

        public Builder withConfiguration(Configuration configuration) {
            this.configuration = configuration;
            return this;
        }

        public FileEtlSource build() {
            return new FileEtlSource(this.id, this.name, this.description, this.isActive, this.dataSourceName, this.alias, this.configuration);
        }
    }

}
