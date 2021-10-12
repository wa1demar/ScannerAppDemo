package com.example.zebrascannerappdemo.data.network.common.intercepror

import com.example.zebrascannerappdemo.data.network.model.request.AuthRequest
import com.example.zebrascannerappdemo.data.storage.prefs.DataStoreManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okio.Buffer
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val dataStoreManager: DataStoreManager,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val json = Json {
            ignoreUnknownKeys = true
        }

        val credentials = getCredentials()
        val auth = if (credentials == null)
            ""
        else
            json.encodeToString(AuthRequest.serializer(), credentials)

        var request = chain.request()
        val rawBody = request.body
        if (rawBody != null) {
            var jsonString = bodyToString(rawBody)
            if (!jsonString.isNullOrEmpty()) {
                jsonString = jsonString
                    .replaceFirst("{", "{ \"authentication\": $auth, ")
                request = request.newBuilder()
                    .method(
                        request.method,
                        jsonString.toRequestBody("application/json".toMediaType())
                    )
                    .build()
            } else {
                request = request.newBuilder()
                    .method(
                        request.method,
                        "{ \"authentication\": $auth }".toRequestBody("application/json".toMediaType())
                    )
                    .build()
            }
        } else {
            request = if (request.method == "POST") {
                request.newBuilder()
                    .method(request.method, auth.toRequestBody("application/json".toMediaType()))
                    .build()
            } else {
                request.newBuilder().build()
            }
        }

        return chain.proceed(request)
    }

    private fun bodyToString(request: RequestBody): String? {
        return try {
            val buffer = Buffer()
            request.writeTo(buffer)
            buffer.readUtf8()
        } catch (ex: IOException) {
            null
        }
    }

    private fun getCredentials(): AuthRequest? {
        val credentials = runBlocking {
            dataStoreManager.getCredentials().first()
        } ?: return null
        return AuthRequest(
            credentials.username,
            credentials.password
        )
    }
}