package com.api.tvmaze.core.network

sealed class ResponseWrapper<T> {
    class SuccessResult<T>(val result: T) : ResponseWrapper<T>()
    class ErrorResult<T>(val message: String) : ResponseWrapper<T>()
}