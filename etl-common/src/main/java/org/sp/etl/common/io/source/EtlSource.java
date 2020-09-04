package org.sp.etl.common.io.source;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.sp.etl.common.io.source.impl.CsvFileEtlSource;
import org.sp.etl.common.io.source.impl.JsonFileEtlSource;

import java.util.Map;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXTERNAL_PROPERTY, property = "type")

@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = CsvFileEtlSource.class, name = "csvFileEtlSource"),
                @JsonSubTypes.Type(value = JsonFileEtlSource.class, name = "jsonFileEtlSource")
        })

public interface EtlSource {

    public String dataSourceName();

    public String sourceName();

    public String sourceAlias();

    public Map<String, String> config();
}
