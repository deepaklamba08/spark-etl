package org.sp.etl.common.model.step;

import org.sp.etl.common.model.Id;
import org.sp.etl.common.model.Identifiable;
import org.sp.etl.function.EtlFunction;

import java.util.List;

public class Step implements Identifiable {


    private Id id;
    private String name;
    private String description;
    private boolean isActive;
    private int stepIndex;
    private String inputSourceName;
    private String outputSourceName;
    private String outputSourceAlias;
    private List<String> sources;
    private List<EtlFunction> etlFunctions;

    public int getStepIndex() {
        return stepIndex;
    }

    public String getInputSourceName() {
        return inputSourceName;
    }

    public String getOutputSourceName() {
        return outputSourceName;
    }

    public String getOutputSourceAlias() {
        return outputSourceAlias;
    }

    public List<String> getSources() {
        return sources;
    }

    public List<EtlFunction> getEtlFunctions() {
        return etlFunctions;
    }

    @Override
    public boolean isActive() {
        return this.isActive;
    }

    @Override
    public Id getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDescription() {
        return this.description;
    }
}
