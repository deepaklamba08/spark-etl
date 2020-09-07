package org.sp.etl.function.column;

public abstract class DateAndTimeFunction extends ColumnFunction {

    private String columnName;
    private String format;

    public DateAndTimeFunction() {
    }

    public DateAndTimeFunction(String name, String description, String columnName, String format) {
        super(name, description);
        this.columnName = columnName;
        this.format = format;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public static class CurrentDateFunction extends DateAndTimeFunction {

        public CurrentDateFunction() {
        }

        public CurrentDateFunction(String name, String description, String columnName) {
            super(name, description, columnName, null);
        }
    }

    public static class CurrentTimestampFunction extends DateAndTimeFunction {

        public CurrentTimestampFunction() {
        }

        public CurrentTimestampFunction(String name, String description, String columnName) {
            super(name, description, columnName, null);
        }
    }
}
