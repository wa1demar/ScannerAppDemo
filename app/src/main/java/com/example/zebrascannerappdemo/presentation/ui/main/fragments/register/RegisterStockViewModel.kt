package com.example.zebrascannerappdemo.presentation.ui.main.fragments.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zebrascannerappdemo.di.ApplicationScope
import com.example.zebrascannerappdemo.domain.enums.JobType
import com.example.zebrascannerappdemo.domain.exception.ValidationError
import com.example.zebrascannerappdemo.domain.model.Container
import com.example.zebrascannerappdemo.domain.model.JobInfo
import com.example.zebrascannerappdemo.domain.model.Product
import com.example.zebrascannerappdemo.domain.model.Resource
import com.example.zebrascannerappdemo.domain.usecase.*
import com.example.zebrascannerappdemo.domain.validator.RegisterStockValidator
import com.example.zebrascannerappdemo.domain.validator.RegisterStockValidatorParams
import com.example.zebrascannerappdemo.presentation.ui.main.delegates.JobViewModelDelegate
import com.example.zebrascannerappdemo.presentation.utils.EventFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterStockViewModel @Inject constructor(
    @ApplicationScope private val applicationScope: CoroutineScope,
    private val updateJobReasonUseCase: UpdateJobReasonUseCase,
    private val checkContainerUseCase: CheckContainerUseCase,
    private val addProductToJobUseCase: AddProductToJobUseCase,
    private val removeProductFromJobUseCase: RemoveProductFromJobUseCase,
    private val removeAllProductFromJobUseCase: RemoveAllProductFromJobUseCase,
    private val clearJobUseCase: ClearJobUseCase,
    private val registerStockValidator: RegisterStockValidator,
    private val registerStockUseCase: RegisterStockUseCase,
    jobViewModelDelegate: JobViewModelDelegate,
) : ViewModel(), JobViewModelDelegate by jobViewModelDelegate {

    companion object {
        private val jobType = JobType.REGISTER
    }

    private val _registerUIState = MutableStateFlow<RegisterUIStates?>(null)
    val registerUIState: StateFlow<RegisterUIStates?> = _registerUIState.asStateFlow()

    private val _registerEvent = EventFlow<RegisterEvent>()
    val registerEvent: SharedFlow<RegisterEvent> = _registerEvent.asSharedFlow()

    var containerBarcodeParams: String? = null
    var itemBarcodeParams: String? = null
    var registerReasonParams: String? = null
        set(value) {
            value?.let {
                updateReason(it)
            }
            field = value
        }

    init {
        jobViewModelDelegate.setJobType(jobType)
    }

    private fun updateReason(reason: String) {
        viewModelScope.launch {
            coroutineScope {
                updateJobReasonUseCase(UpdateJobReasonUseCaseParams(jobType, reason))
            }
        }
    }

    fun checkContainer() {
        if (containerBarcodeParams.isNullOrEmpty()) {
            return
        }
        viewModelScope.launch {
            val reason = registerReasonParams ?: currentJobInfo?.reason
            coroutineScope {
                _registerUIState.update { RegisterUIStates.ShowLoader(true) }
                val result = checkContainerUseCase(
                    CheckContainerUseCaseParams(
                        containerBarcodeParams!!,
                        jobType,
                        reason
                    )
                )
                _registerUIState.update { RegisterUIStates.ShowLoader(false) }
                processContainerCheckResult(result)
            }
        }
    }

    fun onBackButtonClicked() = viewModelScope.launch {
        val job = currentJobInfo ?: return@launch
        applicationScope.launch {
            doClear(job)
        }
    }

    private suspend fun doClear(job: JobInfo) {
        _registerUIState.update { RegisterUIStates.ShowLoader(true) }
        val result =
            clearJobUseCase(ClearJobUseCaseParams(job.jobType))
        _registerUIState.update { RegisterUIStates.ShowLoader(false) }
        handleClearFormResult(result)
    }

    private fun processContainerCheckResult(result: Resource<Container>) {
        when (result) {
            is Resource.Error -> {
                handleException(result.exception)
            }
            is Resource.Success -> {
                handleContainerResult()
            }
        }
    }

    private fun handleException(exception: Throwable) {
        _registerEvent.tryEmit(
            RegisterEvent.ShowError(
                exception
            )
        )
    }

    private fun handleContainerResult() {
        clearParams()
        _registerEvent.tryEmit(RegisterEvent.ShowValidContainerMessage)
    }

    private fun handleClearFormResult(result: Resource<Unit?>) {
        when (result) {
            is Resource.Success -> {
                clearParams()
                _registerEvent.tryEmit(RegisterEvent.JobRemoved)
            }
            is Resource.Error -> {
                handleException(result.exception)
            }
        }
    }

    private fun clearParams() {
        containerBarcodeParams = null
        itemBarcodeParams = null
        registerReasonParams = null
    }

    fun addProduct() = viewModelScope.launch {
        if (itemBarcodeParams.isNullOrEmpty()) {
            return@launch
        }
        _registerUIState.update { RegisterUIStates.ShowLoader(true) }
        val result = addProductToJobUseCase(
            AddProductToJobUseCaseParams(
                itemBarcodeParams!!,
                jobType
            )
        )
        _registerUIState.update { RegisterUIStates.ShowLoader(false) }
        processProductCheckResult(result)
    }

    private fun processProductCheckResult(result: Resource<Product>) {
        when (result) {
            is Resource.Error -> {
                handleException(result.exception)
            }
            is Resource.Success -> {
                handleProductResult()
            }
        }
    }

    private fun handleProductResult() {
        clearParams()
        _registerEvent.tryEmit(RegisterEvent.ProductAdded)
    }

    fun clearJob() = viewModelScope.launch  {
        if (currentJobInfo != null) {
            coroutineScope {
                doClear(currentJobInfo!!)
            }
        }
    }

    fun onRemoveOneClicked(product: Product) = viewModelScope.launch {
        coroutineScope {
            removeProductFromJobUseCase(
                RemoveProductFromJobUseCaseParams(
                    product.barcode,
                    jobType
                )
            )
        }
    }

    fun onRemoveAllClicked(product: Product) = viewModelScope.launch {
        coroutineScope {
            removeAllProductFromJobUseCase(
                RemoveAllProductFromJobUseCaseParams(
                    product.barcode,
                    jobType
                )
            )
        }
    }

    fun onShowInfoClicked(product: Product) {
        _registerEvent.tryEmit(
            RegisterEvent.NavigateToDetailsScreen(
                product
            )
        )
    }

    fun onRegisterClicked() = viewModelScope.launch {
        val errors = registerStockValidator.validate(
            RegisterStockValidatorParams(
                Pair(RegisterStockFragment.FIELD_REASON, currentJobInfo?.reason?.toString()),
                Pair(RegisterStockFragment.FIELD_CONTAINER, currentJobInfo?.container),
                Pair(RegisterStockFragment.FIELD_PRODUCTS, currentJobInfo?.products),
            )
        )
        if (errors.isNotEmpty()) {
            _registerUIState.update {
                RegisterUIStates.ShowEmptyFieldsError(errors)
            }
            return@launch
        }
        coroutineScope {
            doRegister()
        }
    }

    private suspend fun doRegister() {
        _registerUIState.update { RegisterUIStates.ShowLoader(true) }
        when (
            val result = registerStockUseCase(Unit)
        ) {
            is Resource.Error -> {
                // show error
                _registerUIState.update { RegisterUIStates.ShowLoader(false) }
                handleRegisterError(result.exception)
            }
            is Resource.Success -> {
                clearParams()
                _registerUIState.update { RegisterUIStates.ShowLoader(false) }
                _registerEvent.emit(RegisterEvent.JobRegistered(result.data))
            }
        }
    }

    private fun handleRegisterError(ex: Throwable) {
        _registerEvent.tryEmit(
            RegisterEvent.ShowError(ex)
        )
    }

    sealed class RegisterUIStates {
        data class ShowLoader(val show: Boolean) : RegisterUIStates()
        data class ShowEmptyFieldsError(val errors: List<ValidationError>) : RegisterUIStates()
    }

    sealed class RegisterEvent {
        data class ShowError(
            val exception: Throwable,
        ) : RegisterEvent()

        data class NavigateToDetailsScreen(
            val product: Product,
        ) : RegisterEvent()
        data class JobRegistered(val message: String) : RegisterEvent()
        object ProductAdded : RegisterEvent()
        object ShowValidContainerMessage : RegisterEvent()
        object JobRemoved : RegisterEvent()
    }
}