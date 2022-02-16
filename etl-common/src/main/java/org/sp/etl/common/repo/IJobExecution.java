package org.sp.etl.common.repo;

import org.sp.etl.common.model.Id;

public interface IJobExecution {

    public Id startJobExecution(JobExecutionDetail executionDetail);

    public void updateJobExecution(Id jobId, JobExecutionStatus status, String message);
}
