package com.domonz.PrashantAdvait.utils

sealed class NetworkResult<T>(val data: T? = null, val message: String? = null) {

    class Success<T>(data: T) : NetworkResult<T>(data)
    class Error<T>(message: String?, data: T? = null) : NetworkResult<T>(data, message)
    class PermissionError<T>(message: String?, data: T? = null): NetworkResult<T>(data, message)
    class Loading<T> : NetworkResult<T>()

}