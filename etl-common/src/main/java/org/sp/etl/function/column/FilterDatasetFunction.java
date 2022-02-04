package org.sp.etl.function.column;

import org.sp.etl.common.model.Id;

public class FilterDatasetFunction extends ColumnFunction {

    private String filterCondition;

    public FilterDatasetFunction(Id id, String name, String description, boolean isActive, String filterCondition) {
        super(id, name, description, isActive);
        this.filterCondition = filterCondition;
    }

    public String getFilterCondition() {
        return filterCondition;
    }
}
