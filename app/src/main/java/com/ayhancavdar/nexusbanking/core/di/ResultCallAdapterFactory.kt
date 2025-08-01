/*
 * Copyright 2025 Ayhan Cavdar. All Rights Reserved.
 *
 * Save to the extent permitted by law, you may not use, copy, modify,
 * distribute or create derivative works of this material or any part
 * of it without the prior written consent of Ayhan Cavdar.
 * Any reproduction of this material must contain this notice.
 */

package com.ayhancavdar.nexusbanking.core.di

import com.ayhancavdar.nexusbanking.core.common.BaseResponse
import com.ayhancavdar.nexusbanking.core.common.NBNetworkException
import com.ayhancavdar.nexusbanking.core.common.NBNetworkException.Companion.toNBNetworkException
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class ResultCallAdapterFactory : CallAdapter.Factory() {
    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        // Check if the return type is Call
        if (getRawType(returnType) != Call::class.java) return null

        // Check the generic type of Call
        check(returnType is ParameterizedType) { "Return type must be parameterized as Call<Result<T>>" }

        val responseType = getParameterUpperBound(0, returnType)
        if (getRawType(responseType) != Result::class.java) return null

        check(responseType is ParameterizedType) { "Response type must be parameterized as Result<T>" }

        val successType = getParameterUpperBound(0, responseType)
        return ResultCallAdapter<BaseResponse>(successType)
    }
}

private class ResultCallAdapter<T : BaseResponse>(
    private val successType: Type
) : CallAdapter<T, Call<Result<T>>> {
    override fun responseType(): Type = successType

    override fun adapt(call: Call<T>): Call<Result<T>> = ResultCall(call)
}

private class ResultCall<T : BaseResponse>(
    private val delegate: Call<T>
) : Call<Result<T>> {
    override fun enqueue(callback: Callback<Result<T>>) {
        delegate.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                val result = processResponse(response)
                callback.onResponse(this@ResultCall, Response.success(result))
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                callback.onResponse(this@ResultCall, Response.success(Result.failure(t)))
            }
        })
    }

    private fun processResponse(response: Response<T>): Result<T> {
        return if (response.isSuccessful) {
            val body = response.body()

            if (body?.success == true) {
                Result.success(body)
            } else {
                // Business error case: HTTP 2xx but body.success != true
                Result.failure(
                    body?.toNBNetworkException() ?: NBNetworkException(errorMessage = "API error with null body")
                )
            }
        } else {
            // Network error case: HTTP non-2xx
            Result.failure(NBNetworkException(errorCode = response.code(), errorMessage = response.message()))
        }
    }

    override fun clone(): Call<Result<T>> = ResultCall(delegate.clone())
    override fun execute(): Response<Result<T>> =
        throw UnsupportedOperationException("ResultCall doesn't support execute()")

    override fun isExecuted(): Boolean = delegate.isExecuted
    override fun cancel(): Unit = delegate.cancel()
    override fun isCanceled(): Boolean = delegate.isCanceled
    override fun request(): Request = delegate.request()
    override fun timeout(): Timeout = delegate.timeout()
}
