package com.mobven.shortly

data class BaseResponse<T>(val status:Status, val data: T?, val errorMessage:String?) {
    companion object {
        fun <T> success(data: T?): BaseResponse<T> {
            return BaseResponse(Status.SUCCESS, data, null)
        }

        fun <T> error(msg: String): BaseResponse<T> {
            return BaseResponse(Status.ERROR, null, msg)
        }
    }
}

enum class Status{
    SUCCESS,
    ERROR
}