package org.sp.etl.core.repo.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sp.etl.common.exception.EtlExceptions;
import org.sp.etl.common.model.Configuration;
import org.sp.etl.common.model.ConfigurationType;
import org.sp.etl.common.model.Id;
import org.sp.etl.common.model.StringId;
import org.sp.etl.common.repo.IJobExecution;
import org.sp.etl.common.repo.JobExecutionDetail;
import org.sp.etl.common.repo.JobExecutionStatus;
import org.sp.etl.common.util.ConfigurationFactory;
import org.sp.etl.common.util.EtlConstants;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FsJobExecution implements IJobExecution {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private File statusFile;
    private Object lock = new Object();

    public FsJobExecution(String baseDir) throws IOException, EtlExceptions.InvalidConfigurationException, EtlExceptions.SystemFailureException {
        File baseFile = new File(baseDir);
        if (!baseFile.exists()) {
            baseFile.mkdirs();
        }
        this.statusFile = new File(baseFile, EtlConstants.EXECUTION_STATUS_FILE_NAME);
        if (!this.statusFile.exists()) {
            this.statusFile.createNewFile();
            ConfigurationFactory.save(ConfigurationType.JSON, ConfigurationFactory.fromList(Collections.emptyList(), ConfigurationType.JSON),
                    this.statusFile, true);
        }


    }

    @Override
    public Id startJobExecution(JobExecutionDetail jobExeDetail) throws EtlExceptions.SystemFailureException {
        logger.debug("Executing : FsJobExecution.startJobExecution()");
        Id jobId = newRandomId();
        this.saveJobExecutionDetail(new JobExecutionDetail(jobId, jobExeDetail.getJobName(), jobExeDetail.getStatus(), jobExeDetail.getStartTime(), new Date(), jobExeDetail.getMessage()), false);
        return jobId;
    }

    @Override
    public void updateJobExecution(Id jobId, JobExecutionStatus status, String message) throws EtlExceptions.SystemFailureException, EtlExceptions.ObjectNotFoundException {
        logger.debug("Executing : FsJobExecution.updateJobExecution()");
        extracted(jobId, status, message, true);
    }

    private void extracted(Id jobId, JobExecutionStatus status, String message, boolean validateExistance) throws EtlExceptions.SystemFailureException, EtlExceptions.ObjectNotFoundException {
        Map<Id, JobExecutionDetail> detailsMap = this.loadJConfiguration();
        if (detailsMap == null || detailsMap.isEmpty()) {
            throw new EtlExceptions.ObjectNotFoundException("job execution detail not found for id - " + jobId);
        }

        JobExecutionDetail jobExeDetail = detailsMap.get(jobId);
        if (jobExeDetail == null) {
            throw new EtlExceptions.ObjectNotFoundException("job execution detail not found for id - " + jobId);
        }
        this.saveJobExecutionDetail(new JobExecutionDetail(jobId, jobExeDetail.getJobName(), status, jobExeDetail.getStartTime(), new Date(), message), true);
    }

    private Map<Id, JobExecutionDetail> loadJConfiguration() throws EtlExceptions.SystemFailureException {
        if (!this.statusFile.exists()) {
            return null;
        }
        try {
            Configuration config = ConfigurationFactory.parse(this.statusFile, ConfigurationType.JSON);
            if (config.isArray()) {
                return config
                        .getAsList().stream().map(this::mapJobExecutionDetail).collect(Collectors.toMap(e -> e.getId(), Function.identity()));
            } else {
                return null;
            }
        } catch (EtlExceptions.InvalidConfigurationException e) {
            return null;

        }
    }

    private void saveJobExecutionDetail(JobExecutionDetail detail, boolean update) throws EtlExceptions.SystemFailureException {
        List<Configuration> configs;
        Map<Id, JobExecutionDetail> existing = this.loadJConfiguration();
        if (existing == null) {
            existing = new HashMap<>(1);
        }
        if (update) {
            existing.remove(detail.getId());
        }
        existing.put(detail.getId(), detail);
        configs = existing.values().stream().map(this::mapConfiguration).collect(Collectors.toList());
        Configuration head = configs.get(0);
        head.toArray();
        for (int i = 1; i < configs.size(); i++) {
            Configuration config = configs.get(i);
            config.toArray();
            head.merge(config);
        }

        synchronized (lock) {
            try {
                ConfigurationFactory.save(ConfigurationType.JSON, head, this.statusFile, false);
            } catch (EtlExceptions.InvalidConfigurationException e) {
                e.printStackTrace();
            }
        }
    }

    private JobExecutionDetail mapJobExecutionDetail(Configuration configuration) {
        return new JobExecutionDetail(
                new StringId(configuration.getStringValue("id")),
                configuration.getStringValue("jobName"),
                JobExecutionStatus.getJobExecutionStatus(configuration.getStringValue("status")),
                configuration.getDateValue("startTime"),
                configuration.getDateValue("endTime"),
                configuration.getStringValue("message")
        );
    }

    private Configuration mapConfiguration(JobExecutionDetail detail) {
        Map<String, Object> detailMap = new HashMap<>(6);
        detailMap.put("id", detail.getId().getStringValue());
        detailMap.put("jobName", detail.getJobName());
        detailMap.put("status", detail.getStatus().getTypeName());
        detailMap.put("startTime", detail.getStartTime().getTime());
        detailMap.put("endTime", detail.getEndTime() != null ? detail.getEndTime().getTime() : null);
        detailMap.put("message", detail.getMessage());

        try {
            return ConfigurationFactory.fromMap(detailMap, ConfigurationType.JSON);
        } catch (EtlExceptions.InvalidConfigurationException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Id newRandomId() {
        return new StringId(UUID.randomUUID().toString());
    }
}
