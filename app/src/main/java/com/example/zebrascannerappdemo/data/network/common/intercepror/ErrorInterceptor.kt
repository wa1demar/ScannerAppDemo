package com.example.zebrascannerappdemo.data.network.common.intercepror

import com.example.zebrascannerappdemo.data.network.common.BaseErrorResponse
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ErrorInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        var response = chain.proceed(request)

        val responseBody = response.body
        val json = responseBody?.string()?.replace("  ", " ")
        val rawJson = json?.replace(" ", "")

        response = if (rawJson?.contains("\"success\":false") == true) {

            val jsonClient = Json {
                ignoreUnknownKeys = true
            }
            val errorResponse = try {
                jsonClient.decodeFromString(BaseErrorResponse.serializer(), json)
            } catch (ex: Exception) {
                Timber.e(ex)
                null
            }

            val newBody = json.toResponseBody("application/json".toMediaType())
            response.newBuilder()
                .code(401)
                .message(errorResponse?.error?.message ?: errorResponse?.message ?: "")
                .body(newBody).build()
        } else {
            val newJson = getNewJsonForSearchContainer(request, rawJson, json)
            val newBody = newJson?.toResponseBody("application/json".toMediaType())
            response.newBuilder().body(newBody).build()
        }

        return response
    }

    private fun getNewJsonForSearchContainer(
        request: Request,
        rawJson: String?,
        json: String?
    ): String? {
        val url = request.url.toString()
        return when {
            url.contains("/search/container") -> {
                if (rawJson?.contains("\"containerContents\":[]") == true) {
                    val newJson = json
                        ?.replace("\"containerContents\":[]", "\"containerContents\":{}")
                        ?.replace("\"containerContents\": []", "\"containerContents\":{}")
                    newJson
                } else {
                    json
                }
            }
            url.contains("/search/stock") -> {
                if (rawJson?.contains("\"containers\":[]") == true) {
                    val newJson = json
                        ?.replace("\"containers\":[]", "\"containers\":{}")
                        ?.replace("\"containers\": []", "\"containers\":{}")
                    newJson
                } else {
                    json
                }
            }
            url.contains("/lookup/product/") -> {
                if (rawJson?.contains("\"containers\":[]") == true) {
                    val newJson = json
                        ?.replace("\"containers\":[]", "\"containers\":{}")
                        ?.replace("\"containers\": []", "\"containers\":{}")
                    newJson
                } else {
                    json
                }
            }
            else -> json
        }
    }
}