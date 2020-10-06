package org.sp.etl.core.metrics;

import java.io.Serializable;
import java.util.Date;

public class FunctionMetrics implements Serializable {
    private String functionName;
    private Date startTime;
    private Date endTime;

    private FunctionMetrics(String functionName, Date startTime, Date endTime) {
        this.functionName = functionName;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getFunctionName() {
        return functionName;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    @Override
    public String toString() {
        return "FunctionMetrics{" +
                "functionName='" + functionName + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }

    public static class FunctionMetricsBuilder {
        private String functionName;
        private Date startTime;
        private Date endTime;

        public FunctionMetricsBuilder(String functionName, Date startTime) {
            this.functionName = functionName;
            this.startTime = startTime;
        }

        public FunctionMetricsBuilder withEndTime(Date endTime) {
            this.endTime = endTime;
            return this;
        }

        public FunctionMetrics build() {
            return new FunctionMetrics(functionName, startTime, endTime);
        }

    }
}
