package org.sp.etl.function.column.agg;

import org.sp.etl.common.model.Id;
import org.sp.etl.function.column.ColumnFunction;

import java.util.List;

public class GroupByDatasetFunction extends ColumnFunction {

    private List<String> groupByColumns;
    private List<AggregateValue> aggregateValues;

    public GroupByDatasetFunction(Id id, String name, String description, boolean isActive, List<String> groupByColumns, List<AggregateValue> aggregateValues) {
        super(id, name, description, isActive);
        this.groupByColumns = groupByColumns;
        this.aggregateValues = aggregateValues;
    }

    public List<String> getGroupByColumns() {
        return groupByColumns;
    }

    public List<AggregateValue> getAggregateValues() {
        return aggregateValues;
    }

}
