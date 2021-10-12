package com.example.zebrascannerappdemo.domain.usecase

import com.example.zebrascannerappdemo.domain.model.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn

abstract class FlowUseCase<in P, R>(private val coroutineDispatcher: CoroutineDispatcher) {

    operator fun invoke(params: P): Flow<Resource<R>> = execute(params)
        .catch { e ->
            emit(Resource.Error(e))
        }.flowOn(coroutineDispatcher)

    protected abstract fun execute(params: P): Flow<Resource<R>>
}