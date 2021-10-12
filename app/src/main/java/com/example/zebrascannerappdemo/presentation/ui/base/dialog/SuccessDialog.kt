package com.example.zebrascannerappdemo.presentation.ui.base.dialog

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.example.zebrascannerappdemo.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SuccessDialog : DialogFragment(R.layout.dialog_success_toast) {

    init {
        isCancelable = true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireDialog().window
            ?.attributes
            ?.windowAnimations = R.style.ThemeOverlay_App_MaterialAlertDialog_Animation
    }

    override fun onResume() {
        super.onResume()
        closeDialog()
    }

    private var closeJob: Job? = null

    private fun closeDialog() {
        val scope = viewLifecycleOwner.lifecycleScope
        closeJob = scope.launch(Dispatchers.Default) {
            delay(1000)
            withContext(Dispatchers.Main) {
                if (isActive && requireDialog().isShowing) {
                    dismiss()
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        closeJob?.cancel()
        dismiss()
    }
}
