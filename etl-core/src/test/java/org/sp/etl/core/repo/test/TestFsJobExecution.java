package org.sp.etl.core.repo.test;

import org.sp.etl.common.exception.EtlExceptions;
import org.sp.etl.common.model.Id;
import org.sp.etl.common.repo.JobExecutionDetail;
import org.sp.etl.common.repo.JobExecutionStatus;
import org.sp.etl.core.repo.impl.FsJobExecution;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Date;

public class TestFsJobExecution {
    private FsJobExecution jobExecution;
    private Id id;

    @BeforeSuite
    public void initTest() throws IOException, EtlExceptions.InvalidConfigurationException, EtlExceptions.SystemFailureException {
        this.jobExecution = new FsJobExecution("/home/sheru/work/dev/spark-etl/etl-core/target");
    }

    @Test
    public void saveJobExecution() throws EtlExceptions.SystemFailureException {
        id = this.jobExecution.startJobExecution(new JobExecutionDetail(
                "test-job", JobExecutionStatus.Running, new Date(), "started"
        ));
    }

    @Test
    public void updateJobExecution() throws EtlExceptions.SystemFailureException, EtlExceptions.ObjectNotFoundException {
        this.jobExecution.updateJobExecution(id, JobExecutionStatus.Completed, "completed");
    }
}
