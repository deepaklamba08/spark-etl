package org.sp.etl.common.repo;


import org.sp.etl.common.ds.DataSource;
import org.sp.etl.common.io.source.EtlSource;
import org.sp.etl.common.io.tr.EtlTarget;
import org.sp.etl.common.model.Configuration;
import org.sp.etl.common.model.job.Job;

public interface EtlRepository {

    public RepositoryType getType();

    public Job lookupJob(String jobName);

    public EtlSource lookupEtlSource(String sourceName);

    public EtlTarget lookupEtlTarget(String targetName);

    public DataSource lookupDataSource(String dataSourceName);

    public Configuration lookupObject(String objectName);
}
