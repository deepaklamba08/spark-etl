package org.sp.etl.common.io.source.impl;

public class JsonFileEtlSource extends FileEtlSource {
    @Override
    public String fileFormat() {
        return "json";
    }
}
