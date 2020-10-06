package org.sp.etl.core.metrics;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StepMetrics implements Serializable {

    private String stepName;
    private int stepIndex;
    private List<FunctionMetrics> functionMetrics;
    private Date startTime;
    private Date endTime;

    private StepMetrics(String stepName, int stepIndex, List<FunctionMetrics> functionMetrics, Date startTime, Date endTime) {
        this.stepName = stepName;
        this.stepIndex = stepIndex;
        this.functionMetrics = functionMetrics;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getStepName() {
        return stepName;
    }

    public int getStepIndex() {
        return stepIndex;
    }

    public List<FunctionMetrics> getFunctionMetrics() {
        return functionMetrics;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    @Override
    public String toString() {
        return "StepMetrics{" +
                "stepName='" + stepName + '\'' +
                ", functionMetrics=" + (functionMetrics != null ? functionMetrics.size() : 0) +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }

    public static class StepMetricsBuilder {
        private String stepName;
        private int stepIndex;
        private List<FunctionMetrics> functionMetrics;
        private Date startTime;
        private Date endTime;

        public StepMetricsBuilder(String stepName, int stepIndex, Date startTime) {
            this.stepName = stepName;
            this.stepIndex = stepIndex;
            this.startTime = startTime;
        }

        public StepMetricsBuilder withEndTime(Date endTime) {
            this.endTime = endTime;
            return this;
        }

        public StepMetricsBuilder withFunctionMetrics(FunctionMetrics functionMetrics) {
            if (this.functionMetrics == null) {
                this.functionMetrics = new ArrayList<>();
            }
            this.functionMetrics.add(functionMetrics);
            return this;
        }

        public StepMetrics build() {
            return new StepMetrics(stepName, stepIndex, functionMetrics, startTime, endTime);
        }
    }
}
