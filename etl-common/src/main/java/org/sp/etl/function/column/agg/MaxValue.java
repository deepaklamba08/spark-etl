package org.sp.etl.function.column.agg;

public class MaxValue extends AggregateValue {

    public MaxValue() {
    }

    public MaxValue(String columnName, String resultAlias) {
        super(columnName, resultAlias);
    }
}
