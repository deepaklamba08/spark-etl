package org.sp.etl.common.ds;

import org.sp.etl.common.model.Configuration;
import org.sp.etl.common.model.Id;

public class FileSystemDataSource  implements DataSource {
    private Id id;
    private String name;
    private String description;
    private boolean isActive;
    protected FileSystemDataSource(Id id, String name, String description, boolean isActive, Configuration configuration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.isActive = isActive;
    }

    public String getPathByName(String name) {
        return null;
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

        public Builder withConfiguration(Configuration configuration) {
            this.configuration = configuration;
            return this;
        }

        public FileSystemDataSource build() {
            return new FileSystemDataSource(this.id, this.name, this.description, this.isActive, this.configuration);
        }
    }
}
