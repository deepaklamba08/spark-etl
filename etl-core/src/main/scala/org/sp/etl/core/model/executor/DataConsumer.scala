package org.sp.etl.core.model.executor

import org.sp.etl.common.io.tr.EtlTarget
import org.sp.etl.core.model.DataBag

trait DataConsumer {

  def consume(databag: DataBag, target: EtlTarget): Unit
}
