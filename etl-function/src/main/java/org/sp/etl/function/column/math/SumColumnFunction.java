package org.sp.etl.function.column.math;

import org.sp.etl.function.column.ColumnFunction;

import java.util.List;

public class SumColumnFunction extends ColumnFunction {

    private String resultColumnName;
    private List<String> columns;

    public SumColumnFunction() {
    }

    public SumColumnFunction(String name, String description, String resultColumnName, List<String> columns) {
        super(name, description);
        this.resultColumnName = resultColumnName;
        this.columns = columns;
    }

    public String getResultColumnName() {
        return resultColumnName;
    }

    public void setResultColumnName(String resultColumnName) {
        this.resultColumnName = resultColumnName;
    }

    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }
}
