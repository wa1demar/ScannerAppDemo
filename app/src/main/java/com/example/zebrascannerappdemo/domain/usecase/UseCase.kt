package com.example.zebrascannerappdemo.domain.usecase

import com.example.zebrascannerappdemo.domain.model.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber

abstract class UseCase<in P, R>(private val coroutineDispatcher: CoroutineDispatcher) {

    suspend operator fun invoke(params: P): Resource<R> {
        return try {
            withContext(coroutineDispatcher) {
                execute(params).let {
                    Resource.Success(it)
                }
            }
        } catch (e: Exception) {
            Timber.d(e)
            Resource.Error(e)
        }
    }

    @Throws(RuntimeException::class)
    protected abstract suspend fun execute(params: P): R
}