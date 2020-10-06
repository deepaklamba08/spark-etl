package org.sp.etl.core.moniter;

import java.util.Date;

public class InMemoryJobStatusDAO implements IJobStatusDAO {
    @Override
    public String startJobExecution(String jobName) {
        System.out.println("starting execution for job " + jobName + " at - " + new Date());
        return jobName;
    }

    @Override
    public String startStepExecution(String stepName, String jobExecutionId) {
        System.out.println("starting execution for step " + stepName + " at - " + new Date());
        return stepName;
    }

    @Override
    public String startFunctionExecution(String functionName, String stepExecutionId) {
        System.out.println("starting execution for function " + functionName + " at - " + new Date());
        return functionName;
    }

    @Override
    public void endFunctionExecution(String functionExecutionId, String status, String message) {
        System.out.println("ending execution for function " + functionExecutionId + " at - " + new Date());
    }

    @Override
    public void endStepExecution(String stepExecutionId, String status, String message) {
        System.out.println("ending execution for step " + stepExecutionId + " at - " + new Date());
    }

    @Override
    public void endJobExecution(String jobExecutionId, String status, String message) {
        System.out.println("ending execution for job " + jobExecutionId + " at - " + new Date());
    }
}
