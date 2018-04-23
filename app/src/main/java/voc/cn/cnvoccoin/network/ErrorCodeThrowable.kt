package voc.cn.cnvoccoin.network


data class ErrorCodeThrowable(var code: Int, override var message: String?) : Throwable(message) {
}