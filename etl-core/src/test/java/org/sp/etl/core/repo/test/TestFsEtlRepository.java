package org.sp.etl.core.repo.test;

import org.sp.etl.common.ds.DataSource;
import org.sp.etl.common.exception.EtlExceptions;
import org.sp.etl.common.io.source.EtlSource;
import org.sp.etl.common.io.tr.EtlTarget;
import org.sp.etl.common.model.ConfigurationType;
import org.sp.etl.common.model.job.Job;
import org.sp.etl.common.repo.EtlRepository;
import org.sp.etl.common.repo.RepositoryParameter;
import org.sp.etl.common.util.EtlConstants;
import org.sp.etl.core.repo.impl.FsEtlRepository;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class TestFsEtlRepository {

    private EtlRepository etlRepository;

    @BeforeSuite
    public void initTest() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(EtlConstants.CONFIGURATION_TYPE_FIELD, ConfigurationType.JSON.getTypeName());
        parameters.put(EtlConstants.JOB_CONF_FILE_KEY, "src/test/resources/etl_repo/job/job_config.json");
        parameters.put(EtlConstants.OBJECT_CONF_FILE_KEY, "src/test/resources/etl_repo/objects/object_config.json");
        parameters.put(EtlConstants.SOURCE_CONF_FILE_KEY, "src/test/resources/etl_repo/etl_sources/sources.json");
        parameters.put(EtlConstants.DB_CONF_FILE_KEY, "src/test/resources/etl_repo/ds/ds.json");
        parameters.put(EtlConstants.TARGET_CONF_FILE_KEY, "src/test/resources/etl_repo/etl_targets/targets.json");
        this.etlRepository = new FsEtlRepository(new RepositoryParameter(parameters));
    }

    @Test
    public void lookupDatasource() throws EtlExceptions.InvalidConfigurationException {
        DataSource dataSource = this.etlRepository.lookupDataSource("ds1");
        Assert.assertNotNull(dataSource);
        Assert.assertEquals(dataSource.getName(), "file-data-source-1");
    }

    @Test
    public void lookupEtlSource() throws EtlExceptions.InvalidConfigurationException {
        EtlSource source = this.etlRepository.lookupEtlSource("users_data");
        Assert.assertNotNull(source);
        Assert.assertEquals(source.getName(), "users_data");
    }

    @Test
    public void lookupEtlTarget() throws EtlExceptions.InvalidConfigurationException {
        EtlTarget target = this.etlRepository.lookupEtlTarget("target-1");
        Assert.assertNotNull(target);
        Assert.assertEquals(target.getName(), "target-1");
    }

    @Test
    public void lookupJob() throws EtlExceptions.InvalidConfigurationException {
        Job job = this.etlRepository.lookupJob("single-dataset-job");
        Assert.assertNotNull(job);
        Assert.assertEquals(job.getName(), "single-dataset-job");
    }
}
