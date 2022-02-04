package function.column;

import org.sp.etl.common.model.Id;

import java.util.Map;

public class SortDatasetFunction extends ColumnFunction {

    public enum SortOrder {
        asc, desc, asc_null_first, asc_null_last, desc_null_first, desc_null_last
    }

    private Map<String,SortOrder> sortColumns;

    public SortDatasetFunction(Id id, String name, String description, boolean isActive, Map<String, SortOrder> sortColumns) {
        super(id, name, description, isActive);
        this.sortColumns = sortColumns;
    }

    public Map<String, SortOrder> getSortColumns() {
        return sortColumns;
    }
}
