package org.sp.etl.core.model.executor

import org.sp.etl.common.ds.DataSource
import org.sp.etl.common.io.source.EtlSource
import org.sp.etl.core.model.DataBag

trait DataLoader {
    def loadData(source: EtlSource, dataSource: DataSource): DataBag
}
