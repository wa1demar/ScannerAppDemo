package com.example.zebrascannerappdemo.presentation.utils

import android.content.res.Resources
import androidx.annotation.StringRes
import com.example.zebrascannerappdemo.R
import com.example.zebrascannerappdemo.data.network.common.BadRequestException
import com.example.zebrascannerappdemo.data.network.common.HttpException
import com.example.zebrascannerappdemo.domain.exception.*

object ErrorHelper {

    @StringRes
    fun getErrorId(ex: Throwable): Int {
        return when (ex) {
            is NetworkError -> R.string.error_no_internet_connection
            is UnexpectedNetworkError -> R.string.error_server_not_responding

            is NotFoundException -> R.string.error_item_not_found
            is ValidationException -> R.string.error_invalid_request

            else -> R.string.msg_unknown
        }
    }

    fun getErrorString(resource: Resources, ex: Throwable) : String {
        if (ex is BadRequestException && !ex.statusMessage.isNullOrEmpty()) {
            return ex.statusMessage
        }
        return resource.getString(getErrorId(ex))
    }
}

fun Resources.getErrorMessage(ex: Throwable): String {
    return ErrorHelper.getErrorString(this, ex)
}