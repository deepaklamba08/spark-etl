package org.sp.etl.common.repo;

public enum JobExecutionStatus {
    Running("Running"), Completed("Completed"), Failed("Failed");
    private String typeName;

    JobExecutionStatus(String typeName) {
        this.typeName = typeName;
    }

    public static JobExecutionStatus getJobExecutionStatus(String typeName) {
        for (JobExecutionStatus jobExecutionStatus : JobExecutionStatus.values()) {
            if (jobExecutionStatus.typeName.equals(typeName)) {
                return jobExecutionStatus;
            }
        }
        throw new IllegalArgumentException("invalid status type - " + typeName);
    }

    public String getTypeName() {
        return typeName;
    }
}
