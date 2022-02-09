package org.sp.etl.function.dataset;

import org.sp.etl.common.model.Id;
import org.sp.etl.function.DatasetFunction;

public class SQLFunction extends DatasetFunction {
    private String queryType;
    private String querySource;

    public SQLFunction(Id id, String name, String description, boolean isActive, String queryType, String querySource) {
        super(id, name, description, isActive);
        this.queryType = queryType;
        this.querySource = querySource;
    }

    public String getQueryType() {
        return queryType;
    }

    public String getQuerySource() {
        return querySource;
    }
}
