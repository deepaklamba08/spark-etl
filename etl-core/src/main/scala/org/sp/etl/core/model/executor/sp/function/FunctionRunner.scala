package org.sp.etl.core.model.executor.sp.function

import function.EtlFunction

abstract class FunctionRunner[+T <: EtlFunction](function: EtlFunction) {

}
