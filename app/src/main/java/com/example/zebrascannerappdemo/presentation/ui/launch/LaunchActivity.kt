package com.example.zebrascannerappdemo.presentation.ui.launch

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.zebrascannerappdemo.presentation.ui.auth.AuthActivity
import com.example.zebrascannerappdemo.presentation.ui.main.MainActivity
import com.example.zebrascannerappdemo.presentation.utils.launchAndRepeatWithViewLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class LaunchActivity : AppCompatActivity() {

    private val viewModel by viewModels<LaunchViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        launchAndRepeatWithViewLifecycle {
            viewModel.isLoggedIn.collect { action ->
                when (action) {
                    null -> {
                        /* ignore */
                    }
                    is LaunchViewModel.LaunchNavigationAction.NavigateToMainActivityAction -> {
                        startActivity(MainActivity::class.java)
                    }
                    is LaunchViewModel.LaunchNavigationAction.NavigateToAuthActivityAction -> {
                        startActivity(AuthActivity::class.java)
                    }
                }
            }
        }
    }

    private fun startActivity(cls: Class<*>) {
        startActivity(Intent(this@LaunchActivity, cls))
        finishAffinity()
    }
}