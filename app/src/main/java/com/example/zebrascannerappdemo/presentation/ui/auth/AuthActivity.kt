package com.example.zebrascannerappdemo.presentation.ui.auth

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.example.zebrascannerappdemo.R
import com.example.zebrascannerappdemo.databinding.ActivityAuthBinding
import com.example.zebrascannerappdemo.domain.exception.ValidationError
import com.example.zebrascannerappdemo.presentation.ui.main.MainActivity
import com.example.zebrascannerappdemo.presentation.utils.getErrorMessage
import com.example.zebrascannerappdemo.presentation.utils.launchAndRepeatWithViewLifecycle
import com.example.zebrascannerappdemo.presentation.utils.showErrorMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {

    private val viewModel: AuthViewModel by viewModels()

    private var _binding: ActivityAuthBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViews()

        launchAndRepeatWithViewLifecycle {
            viewModel.navigationState
                .collect(::handleNavigationAction)
        }
    }

    private fun setupViews() {
        initFields()
        setupListeners()
    }

    private fun initFields() {
        with(binding) {
            usernameEt.setText(viewModel.usernameParam)
            passwordEt.setText(viewModel.passwordParam)
        }
    }

    private fun setupListeners() {
        with(binding) {
            loginBtn.setOnClickListener {
                viewModel.onLoginClicked()
            }

            usernameEt.addTextChangedListener {
                clearErrors()
                viewModel.usernameParam = it.toString()
            }

            passwordEt.addTextChangedListener {
                clearErrors()
                viewModel.passwordParam = it.toString()
            }
        }
    }

    private fun clearErrors() {
        with(binding) {
            usernameTil.error = null
            passwordTil.error = null
        }
    }

    private fun handleNavigationAction(action: AuthViewModel.LoginNavigationActions?) {
        when (action) {
            AuthViewModel.LoginNavigationActions.GoToHomeScreen -> goToHomeScreen()
            is AuthViewModel.LoginNavigationActions.ShowLoader -> showLoader(action.show)
            is AuthViewModel.LoginNavigationActions.ShowLoginError -> showError(action.error)
            is AuthViewModel.LoginNavigationActions.ShowEmptyFieldsError -> showEmptyFieldsError(action.errors)
            null -> {
                // do nothing
            }
        }
    }

    private fun goToHomeScreen() {
        startActivity(Intent(this, MainActivity::class.java))
        finishAffinity()
    }

    private fun showEmptyFieldsError(errors: List<ValidationError>) {
        showLoader(false)
        errors.forEach { error ->
            when (error.fieldId) {
                FIELD_USERNAME -> {
                    binding.usernameTil.error = getString(R.string.error_empty_field)
                }
                FIELD_PASS -> {
                    binding.passwordTil.error = getString(R.string.error_empty_field)
                }
            }
        }
    }

    private fun showLoader(show: Boolean) {
        with(binding) {
            progress.isVisible = show
            loginBtn.isVisible = !show
        }
    }

    private fun showError(error: Throwable) {
        showLoader(false)
        val message = resources.getErrorMessage(error)
        showErrorMessage(message)
    }

    companion object {
        const val FIELD_USERNAME = 1
        const val FIELD_PASS = 2
    }
}