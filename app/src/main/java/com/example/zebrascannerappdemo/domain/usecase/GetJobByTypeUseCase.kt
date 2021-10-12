package com.example.zebrascannerappdemo.domain.usecase

import com.example.zebrascannerappdemo.di.IoDispatcher
import com.example.zebrascannerappdemo.domain.enums.JobType
import com.example.zebrascannerappdemo.domain.model.JobInfo
import com.example.zebrascannerappdemo.domain.model.Resource
import com.example.zebrascannerappdemo.domain.repository.JobRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetJobByTypeUseCase @Inject constructor(
    private val jobRepository: JobRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : FlowUseCase<GetJobByTypeUseCaseParam, JobInfo?>(dispatcher) {
    override fun execute(params: GetJobByTypeUseCaseParam): Flow<Resource<JobInfo?>> {
        return jobRepository.getJobByType(params.jobType)
            .map { Resource.Success(it) }
    }
}

data class GetJobByTypeUseCaseParam(
    val jobType: JobType
)