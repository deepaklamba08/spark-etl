package org.sp.etl.common.io.tr.impl;

public class JsonFileEtlTarget extends FileEtlTarget{
    @Override
    public String fileFormat() {
        return "json";
    }
}
