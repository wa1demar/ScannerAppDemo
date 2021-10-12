package com.example.zebrascannerappdemo.presentation.ui.main.fragments.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zebrascannerappdemo.domain.model.Resource
import com.example.zebrascannerappdemo.domain.usecase.GetCurrentUserUseCase
import com.example.zebrascannerappdemo.domain.usecase.LogoutUseCase
import com.example.zebrascannerappdemo.presentation.utils.WhileViewSubscribed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    getCurrentUserUseCase: GetCurrentUserUseCase,
    private val logoutUseCase: LogoutUseCase,
) : ViewModel() {

    private val _navigationState = MutableStateFlow<HomeNavigationActions?>(null)
    val navigationState = _navigationState.asStateFlow()

    val currentUser = getCurrentUserUseCase(Unit)
        .map { result ->
            if (result is Resource.Success) {
                result.data
            } else {
                null
            }
        }.stateIn(viewModelScope, WhileViewSubscribed, null)

    fun logout() = viewModelScope.launch {
        coroutineScope {
            logoutUseCase(Unit)
        }
        _navigationState.update { HomeNavigationActions.GoToAuthScreen }
    }

    sealed class HomeNavigationActions {
        object GoToAuthScreen : HomeNavigationActions()
    }
}