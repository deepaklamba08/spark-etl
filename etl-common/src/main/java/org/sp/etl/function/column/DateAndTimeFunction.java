package org.sp.etl.function.column;

import org.sp.etl.common.model.Id;

public abstract class DateAndTimeFunction extends ColumnFunction {

    private String columnName;
    private String format;

    public DateAndTimeFunction(Id id, String name, String description, boolean isActive, String columnName, String format) {
        super(id, name, description, isActive);
        this.columnName = columnName;
        this.format = format;
    }
    public DateAndTimeFunction(Id id, String name, String description, boolean isActive, String columnName) {
        super(id, name, description, isActive);
        this.columnName = columnName;
    }

    public String getColumnName() {
        return columnName;
    }

    public String getFormat() {
        return format;
    }

    public static class CurrentDateFunction extends DateAndTimeFunction {

        public CurrentDateFunction(Id id, String name, String description, boolean isActive, String columnName, String format) {
            super(id, name, description, isActive, columnName, format);
        }
    }

    public static class CurrentTimestampFunction extends DateAndTimeFunction {
        public CurrentTimestampFunction(Id id, String name, String description, boolean isActive, String columnName) {
            super(id, name, description, isActive, columnName);
        }
    }

    public static class ToDateFunction extends DateAndTimeFunction {
        private String sourceColumn;

        public ToDateFunction(Id id, String name, String description, boolean isActive, String columnName, String format, String sourceColumn) {
            super(id, name, description, isActive, columnName, format);
            this.sourceColumn = sourceColumn;
        }

        public String getSourceColumn() {
            return sourceColumn;
        }

    }

    public static class ToTimestampFunction extends DateAndTimeFunction {
        private String sourceColumn;

        public ToTimestampFunction(Id id, String name, String description, boolean isActive, String columnName, String format, String sourceColumn) {
            super(id, name, description, isActive, columnName, format);
            this.sourceColumn = sourceColumn;
        }

        public String getSourceColumn() {
            return sourceColumn;
        }

    }
}
