package com.example.zebrascannerappdemo.presentation.ui.main.delegates

import com.example.zebrascannerappdemo.domain.enums.JobType
import com.example.zebrascannerappdemo.domain.model.JobInfo
import com.example.zebrascannerappdemo.domain.model.Resource
import com.example.zebrascannerappdemo.domain.usecase.GetJobByTypeUseCase
import com.example.zebrascannerappdemo.domain.usecase.GetJobByTypeUseCaseParam
import com.example.zebrascannerappdemo.presentation.utils.WhileViewSubscribed
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import timber.log.Timber

interface JobViewModelDelegate {

    fun setJobType(jobType: JobType)

    var currentJobInfo: JobInfo?

    val stockJob: StateFlow<JobInfo?>
}

internal class JobViewModelDelegateImpl(
    getJobByTypeUseCase: GetJobByTypeUseCase,
    externalScope: CoroutineScope
) : JobViewModelDelegate {

    private val _jobType = MutableStateFlow<JobType?>(null)
    private val jobType: StateFlow<JobType?> = _jobType.asStateFlow()

    override fun setJobType(jobType: JobType) {
        _jobType.update { jobType }
    }

    override var currentJobInfo: JobInfo? = null

    private val _stockJob: Flow<Resource<JobInfo?>> =
        jobType
            .distinctUntilChangedBy { it }
            .filterNotNull()
            .flatMapLatest { type ->
                getJobByTypeUseCase(GetJobByTypeUseCaseParam(type))
                    .map {
                        if (it is Resource.Error) {
                            Timber.e(it.exception)
                        }
                        it
                    }
            }

    override val stockJob: StateFlow<JobInfo?> = _stockJob
        .map { (it as? Resource.Success)?.data }
        .map { job -> job.also { currentJobInfo = it } }
        .stateIn(externalScope, WhileViewSubscribed, null)
}