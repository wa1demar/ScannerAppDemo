package com.example.zebrascannerappdemo.presentation.ui.main.fragments.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.zebrascannerappdemo.R
import com.example.zebrascannerappdemo.databinding.FragmentHomeBinding
import com.example.zebrascannerappdemo.domain.model.User
import com.example.zebrascannerappdemo.presentation.ui.auth.AuthActivity
import com.example.zebrascannerappdemo.presentation.ui.base.BaseFragment
import com.example.zebrascannerappdemo.presentation.ui.base.viewBinding
import com.example.zebrascannerappdemo.presentation.utils.launchAndRepeatWithViewLifecycle
import com.example.zebrascannerappdemo.presentation.utils.navigateTo
import com.example.zebrascannerappdemo.presentation.utils.showConfirmDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.util.*

@AndroidEntryPoint
class HomeFragment : BaseFragment(R.layout.fragment_home) {

    private val viewModel: HomeViewModel by viewModels()
    private val binding: FragmentHomeBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()

        launchAndRepeatWithViewLifecycle {
            viewModel.currentUser
                .collect(::handleUserInfo)
        }

        launchAndRepeatWithViewLifecycle {
            viewModel.navigationState
                .collect(::handleNavigationAction)
        }
    }

    private fun initListeners() {
        binding.registerBtn.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToRegisterStockFragment()
            navigateTo(action)
        }

        binding.logoutBtn.setOnClickListener {
            showConfirmDialog(
                getString(R.string.msg_confirm_logout)
            ) {
                doLogout()
            }
        }
    }

    private fun doLogout() {
        viewModel.logout()
    }

    private fun handleUserInfo(user: User?) {
        if (user == null) return
        with(binding) {
            titleTv.text = user.locationName
            subtitleTv.text = getWelcomeSubtitleBasedOnTimesOfDay(user.name)
        }
    }

    private fun getWelcomeSubtitleBasedOnTimesOfDay(name: String): String {
        val c = Calendar.getInstance()
        return when (c[Calendar.HOUR_OF_DAY]) {
            in 0..11 -> getString(R.string.home_welcome_subtitle_morning, name)
            in 12..15 -> getString(R.string.home_welcome_subtitle_day, name)
            in 16..20 -> getString(R.string.home_welcome_subtitle_evening, name)
            in 21..23 -> getString(R.string.home_welcome_subtitle_night, name)
            else -> getString(R.string.home_welcome_subtitle_day, name)
        }
    }

    private fun handleNavigationAction(action: HomeViewModel.HomeNavigationActions?) {
        when (action) {
            HomeViewModel.HomeNavigationActions.GoToAuthScreen -> openAuthScreen()
        }
    }

    private fun openAuthScreen() {
        startActivity(Intent(requireContext(), AuthActivity::class.java))
        requireActivity().finishAffinity()
    }
}