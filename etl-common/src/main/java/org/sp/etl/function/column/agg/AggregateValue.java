package org.sp.etl.function.column.agg;

public abstract class AggregateValue {

    private String columnName;
    private String resultAlias;

    public AggregateValue() {
    }

    public AggregateValue(String columnName, String resultAlias) {
        this.columnName = columnName;
        this.resultAlias = resultAlias;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getResultAlias() {
        return resultAlias;
    }

    public void setResultAlias(String resultAlias) {
        this.resultAlias = resultAlias;
    }
}
