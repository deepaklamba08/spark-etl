package org.sp.etl.function.column;

public class FilterDatasetFunction extends ColumnFunction {

    private String filterCondition;

    public FilterDatasetFunction() {
    }

    public FilterDatasetFunction(String name, String description, String filterCondition) {
        super(name, description);
        this.filterCondition = filterCondition;
    }

    public String getFilterCondition() {
        return filterCondition;
    }

    public void setFilterCondition(String filterCondition) {
        this.filterCondition = filterCondition;
    }
}
