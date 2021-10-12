package com.example.zebrascannerappdemo.presentation.utils

import android.content.Context
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import com.example.zebrascannerappdemo.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

private var blockingAlert: AlertDialog? = null

internal fun Context.showBlockingError(@StringRes titleId: Int, @StringRes messageId: Int) {
    dismissBlockingAlert()
    val builder = MaterialAlertDialogBuilder(this)
        .setTitle(getString(titleId))
        .setMessage(getString(messageId))
        .setIcon(R.drawable.ic_warning_24)
        .setCancelable(false)
    builder.show().also { blockingAlert = it }
}

internal fun dismissBlockingAlert() {
    blockingAlert?.dismiss()
}