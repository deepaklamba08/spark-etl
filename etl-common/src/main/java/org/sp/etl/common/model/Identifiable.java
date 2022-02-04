package org.sp.etl.common.model;

public interface Identifiable {

    public boolean isActive();

    public Id getId();

    public String getName();

    public String getDescription();
}
