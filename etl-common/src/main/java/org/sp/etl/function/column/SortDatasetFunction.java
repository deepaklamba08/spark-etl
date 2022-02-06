package org.sp.etl.function.column;

import org.sp.etl.common.model.Id;

import java.util.Map;

public class SortDatasetFunction extends ColumnFunction {

    public enum SortOrder {
        asc("asc"), desc("asc"), asc_null_first("asc"), asc_null_last("asc"), desc_null_first("asc"), desc_null_last("asc");
        private String order;

        SortOrder(String order) {
            this.order = order;
        }

        public static SortOrder getSortOrder(String typeOrder) {
            for (SortOrder sortOrderType : SortOrder.values()) {
                if (sortOrderType.order.equals(typeOrder)) {
                    return sortOrderType;
                }
            }
            throw new IllegalArgumentException("invalid sort order type - " + typeOrder);
        }
    }

    private Map<String, SortOrder> sortColumns;

    public SortDatasetFunction(Id id, String name, String description, boolean isActive, Map<String, SortOrder> sortColumns) {
        super(id, name, description, isActive);
        this.sortColumns = sortColumns;
    }

    public Map<String, SortOrder> getSortColumns() {
        return sortColumns;
    }
}
