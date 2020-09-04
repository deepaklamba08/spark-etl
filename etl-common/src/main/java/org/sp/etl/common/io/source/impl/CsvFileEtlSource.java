package org.sp.etl.common.io.source.impl;

public class CsvFileEtlSource extends FileEtlSource {
    @Override
    public String fileFormat() {
        return "csv";
    }
}
