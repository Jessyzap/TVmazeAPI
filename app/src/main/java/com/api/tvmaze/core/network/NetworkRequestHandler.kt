package com.api.tvmaze.core.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class NetworkRequestHandler {

    companion object {

        suspend inline fun <reified T> doRequest(
            crossinline request: suspend () -> Response<T>
        ): ResponseWrapper<T> {
            return try {

                val response = request()
                val result = response.body()

                withContext(Dispatchers.IO) {
                    if (response.isSuccessful) {
                        result?.let {
                            ResponseWrapper.SuccessResult(result = it)
                        } ?: run {
                            ResponseWrapper.ErrorResult(message = "Error loading data")
                        }
                    } else {
                        ResponseWrapper.ErrorResult(
                            message = response.errorBody()?.string() ?: "Unknown error"
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                ResponseWrapper.ErrorResult(message = e.message.orEmpty())
            }
        }
    }

}