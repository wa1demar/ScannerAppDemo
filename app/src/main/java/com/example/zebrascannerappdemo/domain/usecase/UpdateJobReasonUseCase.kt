package com.example.zebrascannerappdemo.domain.usecase

import com.example.zebrascannerappdemo.di.IoDispatcher
import com.example.zebrascannerappdemo.domain.enums.JobType
import com.example.zebrascannerappdemo.domain.repository.JobRepository
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class UpdateJobReasonUseCase @Inject constructor(
    private val jobRepository: JobRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<UpdateJobReasonUseCaseParams, Unit>(dispatcher) {
    override suspend fun execute(params: UpdateJobReasonUseCaseParams) {
        val (jobType, reason) = params
        jobRepository.updateJobReason(jobType, reason)
    }
}

data class UpdateJobReasonUseCaseParams(
    val jobType: JobType,
    val reason: String,
)