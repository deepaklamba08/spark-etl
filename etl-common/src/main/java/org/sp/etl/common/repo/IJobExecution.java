package org.sp.etl.common.repo;

import org.sp.etl.common.exception.EtlExceptions;
import org.sp.etl.common.model.Id;

public interface IJobExecution {

    public Id startJobExecution(JobExecutionDetail executionDetail) throws EtlExceptions.SystemFailureException;

    public void updateJobExecution(Id jobId, JobExecutionStatus status, String message) throws EtlExceptions.SystemFailureException, EtlExceptions.ObjectNotFoundException;
}
