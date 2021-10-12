package com.example.zebrascannerappdemo.domain.exception

import com.example.zebrascannerappdemo.domain.enums.ContainerType

class NetworkError(override val cause: Exception) : Exception(cause)
class UnexpectedNetworkError : Exception()
class NotFoundException : Exception()
class ValidationException : Exception()
class JobNotStartedException : Exception()
class ContainerNotFoundException : Exception()
class ContainerLockedException : Exception()
class WrongContainerTypeException(val type: ContainerType) : Exception()