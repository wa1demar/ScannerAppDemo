package com.example.zebrascannerappdemo.presentation.ui.launch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zebrascannerappdemo.domain.model.data
import com.example.zebrascannerappdemo.domain.usecase.IsUserLoggedInUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class LaunchViewModel @Inject constructor(
    isUserLoggedInUseCase: IsUserLoggedInUseCase,
) : ViewModel() {

    val isLoggedIn = flow {
        val result = isUserLoggedInUseCase(Unit)
        emit(result)
    }.map { result ->
        if (result.data == true) {
            LaunchNavigationAction.NavigateToMainActivityAction
        } else {
            LaunchNavigationAction.NavigateToAuthActivityAction
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    sealed class LaunchNavigationAction {
        object NavigateToAuthActivityAction : LaunchNavigationAction()
        object NavigateToMainActivityAction : LaunchNavigationAction()
    }
}