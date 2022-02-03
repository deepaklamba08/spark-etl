package org.sp.etl.common.model;

public class Identifiable {
    private Id id;
    private String name;
    private String description;
    private boolean isActive;

    protected Identifiable(Id id, String name) {
        this.id = id;
        this.name = name;
    }

    protected Identifiable(Id id, String name, String description, boolean isActive) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.isActive = isActive;
    }

    public boolean isActive() {
        return isActive;
    }

    public Id getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
