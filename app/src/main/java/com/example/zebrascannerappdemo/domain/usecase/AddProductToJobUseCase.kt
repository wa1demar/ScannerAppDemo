package com.example.zebrascannerappdemo.domain.usecase

import com.example.zebrascannerappdemo.di.IoDispatcher
import com.example.zebrascannerappdemo.domain.enums.JobType
import com.example.zebrascannerappdemo.domain.model.Product
import com.example.zebrascannerappdemo.domain.repository.JobRepository
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class AddProductToJobUseCase @Inject constructor(
    private val jobRepository: JobRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<AddProductToJobUseCaseParams, Product>(dispatcher) {
    override suspend fun execute(params: AddProductToJobUseCaseParams): Product {
        val (barcode, jobType) = params
        return jobRepository.addProductToJob(barcode, jobType)
    }
}

data class AddProductToJobUseCaseParams(
    val barcode: String,
    val jobType: JobType
)