package org.sp.etl.function.column;

public class AddConstantValueFunction extends ColumnFunction {

    private String columnName;
    private Object value;

    public AddConstantValueFunction() {
    }

    public AddConstantValueFunction(String columnName, Object value) {
        this.columnName = columnName;
        this.value = value;
    }

    public AddConstantValueFunction(String name, String description, String columnName, Object value) {
        super(name, description);
        this.columnName = columnName;
        this.value = value;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
