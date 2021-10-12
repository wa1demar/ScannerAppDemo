package com.example.zebrascannerappdemo.domain.model

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

sealed class Resource<out R> {

    data class Success<out T>(val data: T) : Resource<T>()
    data class Error(val exception: Throwable) : Resource<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
        }
    }
}

val <T> Resource<T>.data: T?
    get() = (this as? Resource.Success)?.data

val <T> Resource<T>.exception: Throwable?
    get() = (this as? Resource.Error)?.exception

@OptIn(ExperimentalContracts::class)
fun <T> Resource<T>.isError(): Boolean {
    contract {
        returns(true) implies (this@isError is Resource.Error)
    }
    return this is Resource.Error
}

@OptIn(ExperimentalContracts::class)
fun <T> Resource<T>.isSuccess(): Boolean {
    contract {
        returns(true) implies (this@isSuccess is Resource.Success)
    }
    return this is Resource.Success
}

fun <T> Resource<T>.successOr(fallback: T): T {
    return (this as? Resource.Success<T>)?.data ?: fallback
}

fun <T> Resource<T>.successOrNull(): T? {
    return successOr(null)
}

fun <T> Resource<T>.asError(): Resource.Error {
    return this as Resource.Error
}
