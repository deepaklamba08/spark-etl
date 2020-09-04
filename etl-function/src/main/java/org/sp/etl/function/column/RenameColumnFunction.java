package org.sp.etl.function.column;

public class RenameColumnFunction extends ColumnFunction {

    private String oldName;
    private String newName;

    public RenameColumnFunction() {
    }

    public RenameColumnFunction(String name, String description, String oldName, String newName) {
        super(name, description);
        this.oldName = oldName;
        this.newName = newName;
    }

    public String getOldName() {
        return oldName;
    }

    public void setOldName(String oldName) {
        this.oldName = oldName;
    }

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }
}
