package com.example.zebrascannerappdemo.presentation.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zebrascannerappdemo.domain.exception.ValidationError
import com.example.zebrascannerappdemo.domain.model.Resource
import com.example.zebrascannerappdemo.domain.usecase.LoginUseCase
import com.example.zebrascannerappdemo.domain.usecase.LoginUseCaseParams
import com.example.zebrascannerappdemo.domain.validator.LoginValidator
import com.example.zebrascannerappdemo.domain.validator.LoginValidatorParams
import com.example.zebrascannerappdemo.presentation.ui.auth.AuthActivity.Companion.FIELD_PASS
import com.example.zebrascannerappdemo.presentation.ui.auth.AuthActivity.Companion.FIELD_USERNAME
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginValidator: LoginValidator,
    private val loginUseCase: LoginUseCase,
) : ViewModel() {

    private val _navigationState = MutableStateFlow<LoginNavigationActions?>(null)
    val navigationState: StateFlow<LoginNavigationActions?> = _navigationState.asStateFlow()

    var usernameParam: String? = null
    var passwordParam: String? = null

    fun onLoginClicked() = viewModelScope.launch {
        val errors = loginValidator.validate(
            LoginValidatorParams(
                loginPair = Pair(FIELD_USERNAME, usernameParam),
                passwordPair = Pair(FIELD_PASS, passwordParam)
            )
        )
        if (errors.isNotEmpty()) {
            _navigationState.update { LoginNavigationActions.ShowEmptyFieldsError(errors) }
            return@launch
        }
        doLogin(usernameParam!!, passwordParam!!)
    }

    private suspend fun doLogin(username: String, password: String) {
        _navigationState.update { LoginNavigationActions.ShowLoader(true) }
        when (val result = loginUseCase(LoginUseCaseParams(username, password))) {
            is Resource.Error -> {
                handleError(result.exception)
            }
            is Resource.Success -> {
                clearParameters()
                _navigationState.update { LoginNavigationActions.GoToHomeScreen }
            }
        }
    }

    private fun handleError(exception: Throwable) {
        _navigationState.update {
            LoginNavigationActions.ShowLoginError(exception)
        }
    }

    private fun clearParameters() {
        usernameParam = null
        passwordParam = null
    }

    sealed class LoginNavigationActions {
        data class ShowLoginError(val error: Throwable) : LoginNavigationActions()
        data class ShowEmptyFieldsError(val errors: List<ValidationError>) : LoginNavigationActions()
        data class ShowLoader(val show: Boolean) : LoginNavigationActions()
        object GoToHomeScreen : LoginNavigationActions()
    }
}