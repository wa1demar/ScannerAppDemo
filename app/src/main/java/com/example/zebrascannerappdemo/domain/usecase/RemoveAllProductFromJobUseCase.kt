package com.example.zebrascannerappdemo.domain.usecase

import com.example.zebrascannerappdemo.di.IoDispatcher
import com.example.zebrascannerappdemo.domain.enums.JobType
import com.example.zebrascannerappdemo.domain.repository.JobRepository
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class RemoveAllProductFromJobUseCase @Inject constructor(
    private val jobRepository: JobRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<RemoveAllProductFromJobUseCaseParams, Unit>(dispatcher) {
    override suspend fun execute(params: RemoveAllProductFromJobUseCaseParams) {
        val (barcode, jobType) = params
        jobRepository.removeAllItemsFromJob(barcode, jobType)
    }
}

data class RemoveAllProductFromJobUseCaseParams(
    val barcode: String,
    val jobType: JobType
)