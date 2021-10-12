package com.example.zebrascannerappdemo.data.network.common.retrofit

import com.example.zebrascannerappdemo.data.network.common.HttpException
import com.example.zebrascannerappdemo.data.network.common.Result
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException

internal class ResultCall<T>(proxy: Call<T>) : CallDelegate<T, Result<T>>(proxy) {

    override fun enqueueImpl(callback: Callback<Result<T>>) {
        proxy.enqueue(ResultCallback(this, callback))
    }

    override fun cloneImpl(): ResultCall<T> {
        return ResultCall(proxy.clone())
    }

    private class ResultCallback<T>(
        private val proxy: ResultCall<T>,
        private val callback: Callback<Result<T>>
    ) : Callback<T> {

        override fun onResponse(call: Call<T>, response: Response<T>) {
            val result: Result<T>
            if (response.isSuccessful) {
                val body = response.body()
                result = Result.Success.HttpResponse(
                    value = body as T,
                    statusCode = response.code(),
                    statusMessage = response.message(),
                    url = call.request().url.toString(),
                )
            } else {
                result = Result.Failure.HttpError(
                    HttpException(
                        statusCode = response.code(),
                        statusMessage = response.message(),
                        url = call.request().url.toString(),
                    )
                )
            }
            callback.onResponse(proxy, Response.success(result))
        }

        override fun onFailure(call: Call<T>, error: Throwable) {
            val result = when (error) {
                is retrofit2.HttpException -> Result.Failure.HttpError(
                    HttpException(
                        error.code(),
                        error.message(),
                        cause = error
                    )
                )
                is IOException -> Result.Failure.Error(error)
                is SocketTimeoutException -> Result.Failure.NetworkError(error)
                else -> Result.Failure.Error(error)
            }

            callback.onResponse(proxy, Response.success(result))
        }
    }

    override fun timeout(): Timeout {
        return proxy.timeout()
    }
}