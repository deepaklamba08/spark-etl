package org.sp.etl.common.repo;

import java.util.Date;

public class JobExecutionDetail {
    private String jobName;
    private JobExecutionStatus status;
    private Date startTime;
    private Date endTime;
    private String message;

    public JobExecutionDetail(String jobName, JobExecutionStatus status, Date startTime, String message) {
        this.jobName = jobName;
        this.status = status;
        this.startTime = startTime;
        this.message = message;
    }

    public JobExecutionDetail(String jobName, JobExecutionStatus status, Date startTime, Date endTime, String message) {
        this.jobName = jobName;
        this.status = status;
        this.startTime = startTime;
        this.endTime = endTime;
        this.message = message;
    }

    public String getJobName() {
        return jobName;
    }

    public JobExecutionStatus getStatus() {
        return status;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public String getMessage() {
        return message;
    }
}
