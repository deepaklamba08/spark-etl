package org.sp.etl.core.model.executor.sp.function

import org.sp.etl.function.EtlFunction

abstract class FunctionRunner[+T <: EtlFunction](function: EtlFunction) {

}
