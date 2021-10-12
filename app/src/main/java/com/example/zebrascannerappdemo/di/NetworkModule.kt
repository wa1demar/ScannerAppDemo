package com.example.zebrascannerappdemo.di

import com.example.zebrascannerappdemo.data.network.common.intercepror.AuthInterceptor
import com.example.zebrascannerappdemo.data.network.common.intercepror.ErrorInterceptor
import com.example.zebrascannerappdemo.data.network.common.retrofit.ResultAdapterFactory
import com.example.zebrascannerappdemo.data.network.service.*
import com.example.zebrascannerappdemo.data.network.service.impl.*
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
open class NetworkModule {

    companion object {
        val BASE_URL = "https://domain.example.com/"
        val url = "${BASE_URL}api/"
    }

    @Provides
    @Singleton
    fun provideConverterFactory(): Converter.Factory {
        val json = Json {
            ignoreUnknownKeys = true
        }
        return json.asConverterFactory("application/json".toMediaType())
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        errorInterceptor: ErrorInterceptor,
    ): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(authInterceptor)
            .addInterceptor(errorInterceptor)
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient, converterFactory: Converter.Factory): Retrofit {
        return Retrofit.Builder()
            .baseUrl(url)
            .client(client)
            .addCallAdapterFactory(ResultAdapterFactory())
            .addConverterFactory(converterFactory)
            .build()
    }

    @Named("unauthorized")
    @Provides
    @Singleton
    fun provideUnauthorizedOkHttpClient(errorInterceptor: ErrorInterceptor): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addInterceptor(errorInterceptor)
            .addInterceptor(logging)
            .build()
    }

    @Named("unauthorized")
    @Provides
    @Singleton
    fun provideUnauthorizedRetrofit(
        @Named("unauthorized") client: OkHttpClient,
        converterFactory: Converter.Factory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(url)
            .client(client)
            .addCallAdapterFactory(ResultAdapterFactory())
            .addConverterFactory(converterFactory)
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(@Named("unauthorized") retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)

    @Provides
    @Singleton
    fun provideContainerApiService(retrofit: Retrofit): ContainerApiService =
        retrofit.create(ContainerApiService::class.java)

    @Provides
    @Singleton
    fun provideProductApiService(retrofit: Retrofit): ProductApiService =
        retrofit.create(ProductApiService::class.java)

    @Provides
    @Singleton
    fun provideUserApiService(retrofit: Retrofit): UserApiService =
        retrofit.create(UserApiService::class.java)

    @Provides
    @Singleton
    fun provideStockApiService(retrofit: Retrofit): StockApiService =
        retrofit.create(StockApiService::class.java)

    @Mock
    @Provides
    @Singleton
    fun provideApiServiceMock(@Named("unauthorized") retrofit: Retrofit): ApiService =
        MockApiService()

    @Mock
    @Provides
    @Singleton
    fun provideContainerApiServiceMock(retrofit: Retrofit): ContainerApiService =
        MockContainerApiService()

    @Mock
    @Provides
    @Singleton
    fun provideProductApiServiceMock(retrofit: Retrofit): ProductApiService =
        MockProductApiService()

    @Mock
    @Provides
    @Singleton
    fun provideUserApiServiceMock(retrofit: Retrofit): UserApiService =
        MockUserApiService()

    @Mock
    @Provides
    @Singleton
    fun provideMockStockApiService(retrofit: Retrofit): StockApiService =
        MockStockApiService()
}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class Mock
