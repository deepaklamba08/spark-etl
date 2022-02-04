package org.sp.etl.common.io.source;


import org.sp.etl.common.model.Identifiable;

public interface EtlSource extends Identifiable {

    public String dataSourceName();
}
