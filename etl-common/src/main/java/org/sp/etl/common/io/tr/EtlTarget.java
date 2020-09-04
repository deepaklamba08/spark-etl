package org.sp.etl.common.io.tr;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.sp.etl.common.io.source.impl.CsvFileEtlSource;
import org.sp.etl.common.io.source.impl.JsonFileEtlSource;
import org.sp.etl.common.io.tr.impl.CsvFileEtlTarget;
import org.sp.etl.common.io.tr.impl.JsonFileEtlTarget;

import java.util.Map;


@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXTERNAL_PROPERTY, property = "type")

@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = CsvFileEtlTarget.class, name = "csvFileEtlTarget"),
                @JsonSubTypes.Type(value = JsonFileEtlTarget.class, name = "jsonFileEtlTarget")
        })

public interface EtlTarget {
    public String dataSourceName();

    public String targetName();

    public Map<String, String> config();

}
