package org.sp.etl.common.ds;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.Serializable;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXTERNAL_PROPERTY, property = "type")

@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = FileSystemDataSource.class, name = "fileSystemDs"),
                @JsonSubTypes.Type(value = LocalFileSystemDataSource.class, name = "localFileSystemDs")
        })

public interface DataSource extends Serializable {

    public String dataSourceName();
}
