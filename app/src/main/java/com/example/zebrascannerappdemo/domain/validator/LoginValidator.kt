package com.example.zebrascannerappdemo.domain.validator

import com.example.zebrascannerappdemo.domain.exception.ValidationError
import com.example.zebrascannerappdemo.domain.exception.ValidationErrorType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginValidator @Inject constructor() : Validator<LoginValidatorParams> {

    override fun validate(params: LoginValidatorParams): List<ValidationError> {
        val errors = mutableListOf<ValidationError>()

        val (loginId, login) = params.loginPair
        if (login.isNullOrEmpty()) {
            errors.add(
                ValidationError(
                    fieldId = loginId,
                    validationErrorType = ValidationErrorType.EMPTY_FIELD
                )
            )
        }
        val (passwordId, password) = params.passwordPair
        if (password.isNullOrEmpty()) {
            errors.add(
                ValidationError(
                    fieldId = passwordId,
                    validationErrorType = ValidationErrorType.EMPTY_FIELD
                )
            )
        }

        return errors
    }
}

data class LoginValidatorParams(
    val loginPair: Pair<Int, String?>,
    val passwordPair: Pair<Int, String?>
)