package com.dawn.lib_base.http

class BaseResponse<T> {
    val data: T? = null;
    val errorCode: Int? = null;
    val errorMsg: String? = null;
    var exception: Throwable? = null;
}

inline fun <T> BaseResponse<T>.next(bloc: BaseResponse<T>.() -> Unit): BaseResponse<T> {
    return if (exception == null) {//没有异常，则把正确结果bloc出去
        bloc()
        this
    } else {//出现异常（网络/服务器/自定义异常）执行这里 不用bloc
        this;
    }

}


inline fun <T> BaseResponse<T>.catchException(bloc: Throwable.() -> Unit) {
    if (exception != null) {
        bloc(exception!!)
    }
}