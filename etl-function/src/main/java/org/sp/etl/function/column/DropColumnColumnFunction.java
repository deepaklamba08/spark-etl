package org.sp.etl.function.column;

public class DropColumnColumnFunction extends ColumnFunction {

    private String columnName;

    public DropColumnColumnFunction() {
    }

    public DropColumnColumnFunction(String name, String description, String columnName) {
        super(name, description);
        this.columnName = columnName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }
}
