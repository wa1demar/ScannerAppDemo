package com.example.zebrascannerappdemo.domain.usecase

import com.example.zebrascannerappdemo.di.IoDispatcher
import com.example.zebrascannerappdemo.domain.enums.JobType
import com.example.zebrascannerappdemo.domain.repository.JobRepository
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class ClearJobUseCase @Inject constructor(
    private val jobRepository: JobRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<ClearJobUseCaseParams, Unit>(dispatcher) {
    override suspend fun execute(params: ClearJobUseCaseParams) {
        return jobRepository.clearJob(params.jobType)
    }
}

data class ClearJobUseCaseParams(
    val jobType: JobType,
)