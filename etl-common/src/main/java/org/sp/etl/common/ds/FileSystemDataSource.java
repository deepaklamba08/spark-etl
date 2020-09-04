package org.sp.etl.common.ds;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class FileSystemDataSource implements DataSource {

    private String baseDirectory;
    private String name;

    @JsonIgnore
    public abstract String getPath(String fileName);

    @Override
    public String dataSourceName() {
        return name;
    }

    public void setBaseDirectory(String baseDirectory) {
        this.baseDirectory = baseDirectory;
    }

    public String getBaseDirectory() {
        return baseDirectory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
