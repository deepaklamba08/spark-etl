package org.sp.etl.common.model.step;

import org.sp.etl.function.EtlFunction;

import java.util.List;

public class Step {

    private String stepName;
    private int stepIndex;
    private String inputSourceName;
    private String outputSourceName;
    private String outputSourceAlias;
    private List<String> sources;
    private List<EtlFunction> etlFunctions;

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public int getStepIndex() {
        return stepIndex;
    }

    public void setStepIndex(int stepIndex) {
        this.stepIndex = stepIndex;
    }

    public String getInputSourceName() {
        return inputSourceName;
    }

    public void setInputSourceName(String inputSourceName) {
        this.inputSourceName = inputSourceName;
    }

    public String getOutputSourceName() {
        return outputSourceName;
    }

    public void setOutputSourceName(String outputSourceName) {
        this.outputSourceName = outputSourceName;
    }

    public String getOutputSourceAlias() {
        return outputSourceAlias;
    }

    public void setOutputSourceAlias(String outputSourceAlias) {
        this.outputSourceAlias = outputSourceAlias;
    }

    public List<String> getSources() {
        return sources;
    }

    public void setSources(List<String> sources) {
        this.sources = sources;
    }

    public List<EtlFunction> getEtlFunctions() {
        return etlFunctions;
    }

    public void setEtlFunctions(List<EtlFunction> etlFunctions) {
        this.etlFunctions = etlFunctions;
    }

    @Override
    public String toString() {
        return "stepName=" + stepName + ", stepIndex=" + stepIndex;
    }
}
