package org.sp.etl.function.column;

import org.sp.etl.common.model.Id;

public class CastColumnFunction extends ColumnFunction {

    private String resultColumn;
    private String sourceColumn;
    private String toType;

    public String getResultColumn() {
        return resultColumn;
    }

    public String getSourceColumn() {
        return sourceColumn;
    }

    public String getToType() {
        return toType;
    }

    public CastColumnFunction(Id id, String name, String description, boolean isActive, String resultColumnName, String sourceColumn, String toType) {
        super(id, name, description, isActive);
        this.resultColumn = resultColumnName;
        this.sourceColumn = sourceColumn;
        this.toType = toType;
    }
}
