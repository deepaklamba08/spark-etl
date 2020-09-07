package org.sp.etl.function.column.agg;

import org.sp.etl.function.column.ColumnFunction;

import java.util.List;

public class GroupByDatasetFunction extends ColumnFunction {

    private List<String> groupByColumns;
    private List<AggregateValue> aggregateValues;

    public GroupByDatasetFunction() {
    }

    public GroupByDatasetFunction(String name, String description, List<String> groupByColumns, List<AggregateValue> aggregateValues) {
        super(name, description);
        this.groupByColumns = groupByColumns;
        this.aggregateValues = aggregateValues;
    }

    public List<String> getGroupByColumns() {
        return groupByColumns;
    }

    public void setGroupByColumns(List<String> groupByColumns) {
        this.groupByColumns = groupByColumns;
    }

    public List<AggregateValue> getAggregateValues() {
        return aggregateValues;
    }

    public void setAggregateValues(List<AggregateValue> aggregateValues) {
        this.aggregateValues = aggregateValues;
    }
}
