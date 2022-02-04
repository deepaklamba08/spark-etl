package org.sp.etl.common.io.tr;

import org.sp.etl.common.model.Identifiable;

public interface EtlTarget extends Identifiable {
    public String dataSourceName();

}
