package org.sp.etl.function.dataset;

import org.sp.etl.common.model.Id;
import org.sp.etl.function.DatasetFunction;

public class SQLFunction extends DatasetFunction {
    private String queryType;
    private String query;

    public SQLFunction(Id id, String name, String description, boolean isActive, String queryType, String query) {
        super(id, name, description, isActive);
        this.queryType = queryType;
        this.query = query;
    }

    public String getQueryType() {
        return queryType;
    }

    public String getQuery() {
        return query;
    }
}
