package org.sp.etl.core.moniter;

public interface IJobStatusDAO {

    public String startJobExecution(String jobName);

    public String startStepExecution(String stepName, String jobExecutionId);

    public String startFunctionExecution(String functionName, String stepExecutionId);

    public void endFunctionExecution(String functionExecutionId, String status, String message);

    public void endStepExecution(String stepExecutionId, String status, String message);

    public void endJobExecution(String jobExecutionId, String status, String message);
}
