package org.sp.etl.common.repo;


import org.sp.etl.common.ds.DataSource;
import org.sp.etl.common.exception.EtlExceptions;
import org.sp.etl.common.io.source.EtlSource;
import org.sp.etl.common.io.tr.EtlTarget;
import org.sp.etl.common.model.Configuration;
import org.sp.etl.common.model.job.Job;

public interface EtlRepository {

    public RepositoryType getType();

    public Job lookupJob(String jobName) throws EtlExceptions.InvalidConfigurationException;

    public EtlSource lookupEtlSource(String sourceName) throws EtlExceptions.InvalidConfigurationException;

    public EtlTarget lookupEtlTarget(String targetName) throws EtlExceptions.InvalidConfigurationException;

    public DataSource lookupDataSource(String dataSourceName) throws EtlExceptions.InvalidConfigurationException;

    public Configuration lookupObject(String objectName) throws EtlExceptions.InvalidConfigurationException;
}
