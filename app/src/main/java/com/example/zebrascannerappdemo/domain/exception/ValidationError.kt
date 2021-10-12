package com.example.zebrascannerappdemo.domain.exception

data class ValidationError(
    val fieldId: Int,
    val validationErrorType: ValidationErrorType
)

enum class ValidationErrorType {
    EMPTY_FIELD,
    DUPLICATE_FIELD,
    EMPTY_REQUIRED_FIELD,
    NOT_FOUND,
    WRONG_DATA,
    OUT_OF_LIST,
}