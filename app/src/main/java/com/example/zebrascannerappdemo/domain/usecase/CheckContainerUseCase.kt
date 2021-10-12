package com.example.zebrascannerappdemo.domain.usecase

import com.example.zebrascannerappdemo.di.IoDispatcher
import com.example.zebrascannerappdemo.domain.enums.JobType
import com.example.zebrascannerappdemo.domain.model.Container
import com.example.zebrascannerappdemo.domain.repository.ContainerRepository
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class CheckContainerUseCase @Inject constructor(
    private val containerRepository: ContainerRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<CheckContainerUseCaseParams, Container>(dispatcher) {
    override suspend fun execute(params: CheckContainerUseCaseParams): Container {
        val (containerBarcode, jobType, jobReason) = params
        return containerRepository.checkContainer(
            containerBarcode,
            jobType,
            jobReason,
        )
    }
}

data class CheckContainerUseCaseParams(
    val containerBarcode: String,
    val jobType: JobType,
    val jobReason: String?,
)