package org.sp.etl.function.column;

import java.util.Map;

public class SortDatasetFunction extends ColumnFunction {

    public enum SortOrder {
        asc, desc, asc_null_first, asc_null_last, desc_null_first, desc_null_last
    }

    private Map<String,SortOrder> sortColumns;


    public SortDatasetFunction(String name, String description, Map<String, SortOrder> sortColumns) {
        super(name, description);
        this.sortColumns = sortColumns;
    }

    public Map<String, SortOrder> getSortColumns() {
        return sortColumns;
    }

    public void setSortColumns(Map<String, SortOrder> sortColumns) {
        this.sortColumns = sortColumns;
    }
}
