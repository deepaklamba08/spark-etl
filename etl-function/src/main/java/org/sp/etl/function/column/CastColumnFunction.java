package org.sp.etl.function.column;

public class CastColumnFunction extends ColumnFunction {

    private String resultColumnName;
    private String sourceColumn;
    private String toType;

    public CastColumnFunction(){
    }

    public CastColumnFunction(String name, String description, String resultColumnName, String sourceColumn, String toType) {
        super(name, description);
        this.resultColumnName = resultColumnName;
        this.sourceColumn = sourceColumn;
        this.toType = toType;
    }

    public String getResultColumnName() {
        return resultColumnName;
    }

    public void setResultColumnName(String resultColumnName) {
        this.resultColumnName = resultColumnName;
    }

    public String getToType() {
        return toType;
    }

    public void setToType(String toType) {
        this.toType = toType;
    }

    public String getSourceColumn() {
        return sourceColumn;
    }

    public void setSourceColumn(String sourceColumn) {
        this.sourceColumn = sourceColumn;
    }
}
