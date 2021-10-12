package com.example.zebrascannerappdemo.presentation.ui.base

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

abstract class BaseFragment(@LayoutRes layoutId: Int) : Fragment(layoutId), OnBackPressedListener {

    override fun onBackPressed(): Boolean {
        return false
    }
}