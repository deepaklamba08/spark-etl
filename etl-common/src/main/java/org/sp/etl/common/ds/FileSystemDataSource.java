package org.sp.etl.common.ds;

import org.sp.etl.common.model.Configuration;
import org.sp.etl.common.model.Id;

import java.util.Map;

public class FileSystemDataSource implements DataSource {
    private Id id;
    private String name;
    private String description;
    private boolean isActive;
    private Map<String, String> namedPaths;
    private Configuration configuration;

    private FileSystemDataSource(Id id, String name, String description, boolean isActive, Map<String, String> namedPaths, Configuration configuration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.isActive = isActive;
        this.namedPaths = namedPaths;
        this.configuration = configuration;
    }

    public String getPathByName(String pathName) {
        return this.namedPaths.get(pathName);
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
        private Map<String, String> namedPaths;
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

        public Builder withConfiguration(Configuration configuration) {
            this.configuration = configuration;
            return this;
        }

        public Builder withNamedPaths(Map<String, String> namedPaths) {
            this.namedPaths = namedPaths;
            return this;
        }

        public FileSystemDataSource build() {
            return new FileSystemDataSource(this.id, this.name, this.description, this.isActive, this.namedPaths, this.configuration);
        }
    }
}
