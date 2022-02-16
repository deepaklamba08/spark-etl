package org.sp.etl.core.repo.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sp.etl.common.model.Id;
import org.sp.etl.common.repo.IJobExecution;
import org.sp.etl.common.repo.JobExecutionDetail;
import org.sp.etl.common.repo.JobExecutionStatus;
import org.sp.etl.common.util.EtlConstants;

import java.io.File;

public class FsJobExecution implements IJobExecution {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private File statusFile;

    public FsJobExecution(String baseDir) {
        File baseFile = new File(baseDir);
        if (!baseFile.exists()) {
            baseFile.mkdirs();
        }
        this.statusFile = new File(baseFile, EtlConstants.EXECUTION_STATUS_FILE_NAME);
    }

    @Override
    public Id startJobExecution(JobExecutionDetail executionDetail) {
        logger.debug("Executing : FsJobExecution.startJobExecution()");
        if (!this.statusFile.exists()) {

        }
        return null;
    }

    @Override
    public void updateJobExecution(Id jobId, JobExecutionStatus status, String message) {
        logger.debug("Executing : FsJobExecution.updateJobExecution()");

    }
}
