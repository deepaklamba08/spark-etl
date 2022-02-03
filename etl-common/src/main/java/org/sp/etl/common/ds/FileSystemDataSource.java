package org.sp.etl.common.ds;

import org.sp.etl.common.model.Configuration;
import org.sp.etl.common.model.Id;
import org.sp.etl.common.model.Identifiable;

public class FileSystemDataSource extends Identifiable implements DataSource {

    protected FileSystemDataSource(Id id, String name, String description, boolean isActive, Configuration configuration) {
        super(id, name, description, isActive);
    }

    public String getPathByName(String name) {
        return null;
    }

    public static class FileSystemDataSourceBuilder {
        private Id id;
        private String name;
        private String description;
        private boolean isActive;
        private Configuration configuration;

        public FileSystemDataSourceBuilder withId(Id id) {
            this.id = id;
            return this;
        }

        public FileSystemDataSourceBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public FileSystemDataSourceBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public FileSystemDataSourceBuilder makeActive() {
            this.isActive = true;
            return this;
        }

        public FileSystemDataSourceBuilder makeInActive() {
            this.isActive = false;
            return this;
        }

        public FileSystemDataSourceBuilder withConfiguration(Configuration configuration) {
            this.configuration = configuration;
            return this;
        }

        public FileSystemDataSource build() {
            return new FileSystemDataSource(this.id, this.name, this.description, this.isActive, this.configuration);
        }
    }
}
