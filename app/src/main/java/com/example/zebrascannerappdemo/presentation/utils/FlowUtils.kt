package com.example.zebrascannerappdemo.presentation.utils

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch

private const val StopTimeoutMillis: Long = 5000
val WhileViewSubscribed: SharingStarted = SharingStarted.WhileSubscribed(StopTimeoutMillis)

inline fun Fragment.launchAndRepeatWithViewLifecycle(
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline block: suspend CoroutineScope.() -> Unit
) {
    viewLifecycleOwner.lifecycleScope.launch {
        repeatOnLifecycle(minActiveState) {
            block()
        }
    }
}

inline fun AppCompatActivity.launchAndRepeatWithViewLifecycle(
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline block: suspend CoroutineScope.() -> Unit
) {
    this.lifecycleScope.launch {
        repeatOnLifecycle(minActiveState) {
            block()
        }
    }
}

@Suppress("FunctionName")
fun <T> EventFlow(): MutableSharedFlow<T> = MutableSharedFlow<T>(
    replay = 0,
    extraBufferCapacity = 3,
    onBufferOverflow = BufferOverflow.DROP_OLDEST
)