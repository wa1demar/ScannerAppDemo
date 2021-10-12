package com.example.zebrascannerappdemo.domain.usecase

import com.example.zebrascannerappdemo.di.IoDispatcher
import com.example.zebrascannerappdemo.domain.enums.JobType
import com.example.zebrascannerappdemo.domain.repository.JobRepository
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class RemoveProductFromJobUseCase @Inject constructor(
    private val jobRepository: JobRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<RemoveProductFromJobUseCaseParams, Unit>(dispatcher) {
    override suspend fun execute(params: RemoveProductFromJobUseCaseParams) {
        val (barcode, jobType) = params
        jobRepository.removeProductFromJob(barcode, jobType)
    }
}

data class RemoveProductFromJobUseCaseParams(
    val barcode: String,
    val jobType: JobType
)