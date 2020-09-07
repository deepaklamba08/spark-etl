package org.sp.etl.function.column.agg;

public class SumValue extends AggregateValue {

    public SumValue() {
    }

    public SumValue(String columnName, String resultAlias) {
        super(columnName, resultAlias);
    }
}
