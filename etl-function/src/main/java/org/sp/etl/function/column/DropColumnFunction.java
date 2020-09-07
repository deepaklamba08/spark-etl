package org.sp.etl.function.column;

public class DropColumnFunction extends ColumnFunction {

    private String columnName;

    public DropColumnFunction() {
    }

    public DropColumnFunction(String name, String description, String columnName) {
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
