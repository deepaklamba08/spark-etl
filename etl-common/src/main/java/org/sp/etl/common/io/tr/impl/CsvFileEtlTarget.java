package org.sp.etl.common.io.tr.impl;

public class CsvFileEtlTarget extends FileEtlTarget {
    @Override
    public String fileFormat() {
        return "csv";
    }
}
