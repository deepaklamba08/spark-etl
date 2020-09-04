package org.sp.etl.core.metrics;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JobMetrics implements Serializable {
    private String jobName;
    private List<StepMetrics> stepMetrics;
    private Date startTime;
    private Date endTime;

    private JobMetrics(String jobName, List<StepMetrics> stepMetrics, Date startTime, Date endTime) {
        this.jobName = jobName;
        this.stepMetrics = stepMetrics;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getJobName() {
        return jobName;
    }

    public List<StepMetrics> getStepMetrics() {
        return stepMetrics;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public static class JobMetricsBuilder {
        private String jobName;
        private List<StepMetrics> stepMetrics;
        private Date startTime;
        private Date endTime;

        public JobMetricsBuilder(String jobName, Date startTime) {
            this.jobName = jobName;
            this.startTime = startTime;
        }

        public JobMetricsBuilder withStepMetrics(StepMetrics stepMetrics) {
            if (this.stepMetrics == null) {
                this.stepMetrics = new ArrayList<>();
            }
            this.stepMetrics.add(stepMetrics);
            return this;
        }

        public JobMetricsBuilder withEndTime(Date endTime) {
            this.endTime = endTime;
            return this;
        }

        public JobMetrics build() {
            return new JobMetrics(jobName, stepMetrics, startTime, endTime);
        }
    }
}
