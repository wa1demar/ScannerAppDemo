package com.example.zebrascannerappdemo.presentation.utils

import android.app.Activity
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavDirections
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import com.example.zebrascannerappdemo.R
import com.example.zebrascannerappdemo.presentation.ui.base.dialog.SuccessDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder

val FragmentManager.currentNavigationFragment: Fragment?
    get() = primaryNavigationFragment?.childFragmentManager?.fragments?.first()

internal fun Fragment.showErrorMessage(message: String?, ok: (() -> Unit)? = null) {
    requireActivity().showErrorMessage(message, ok)
}

internal fun Activity.showErrorMessage(message: String?, ok: (() -> Unit)? = null) {
    showMessage(message, getString(R.string.error), ok)
}

internal fun Activity.showMessage(
    message: String?,
    title: String? = null,
    ok: (() -> Unit)? = null
) {
    val builder = MaterialAlertDialogBuilder(this)
        .setTitle(title)
        .setMessage(message ?: getString(R.string.msg_unknown))
        .setCancelable(false)
    builder.setPositiveButton(R.string.ok) { dialog, _ ->
        dialog.dismiss()
        ok?.invoke()
    }
    builder.show()
}

internal fun Fragment.showConfirmDialog(
    message: String?,
    confirm: () -> Unit,
) {
    requireActivity().showConfirmDialog(message, confirm, null)
}

internal fun Activity.showConfirmDialog(
    message: String?,
    confirm: () -> Unit,
    cancel: (() -> Unit)? = null
) {
    val builder = MaterialAlertDialogBuilder(this)
        .setMessage(message)
        .setCancelable(false)

    builder.setPositiveButton(R.string.yes) { dialog, _ ->
        dialog.dismiss()
        confirm.invoke()
    }
    builder.setNegativeButton(R.string.cancel) { dialog, _ ->
        dialog.dismiss()
        cancel?.invoke()
    }
    builder.show()
}

fun Fragment.navigateTo(action: NavDirections) {
    this.navigateTo(action, this.javaClass)
}

fun <T : Any> Fragment.navigateTo(action: NavDirections, javaClass: Class<T>) {
    val controller = findNavController()
    if ((controller.currentDestination as FragmentNavigator.Destination).className == javaClass.name) {
        controller.navigate(action)
    }
}

internal fun Fragment.showMessage(
    message: String?,
    title: String? = null,
    ok: (() -> Unit)? = null
) {
    requireActivity().showMessage(message, title, ok)
}

internal fun Fragment.toastSuccess() {
    val fragment = childFragmentManager.findFragmentByTag("check")
    if (fragment == null) {
        val dialog = SuccessDialog()
        dialog.show(childFragmentManager, "check")
    }
}