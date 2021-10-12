package com.example.zebrascannerappdemo.domain.validator

import com.example.zebrascannerappdemo.domain.exception.ValidationError
import com.example.zebrascannerappdemo.domain.exception.ValidationErrorType
import com.example.zebrascannerappdemo.domain.model.Product
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RegisterStockValidator @Inject constructor() : Validator<RegisterStockValidatorParams> {
    override fun validate(params: RegisterStockValidatorParams): List<ValidationError> {
        val errors = mutableListOf<ValidationError>()

        val (reasonId, reason) = params.reasonPair
        val (containerId, container) = params.containerPair
        val (productsId, products) = params.productsPair

        if (reason.isNullOrEmpty()) {
            errors.add(
                ValidationError(
                    fieldId = reasonId,
                    validationErrorType = ValidationErrorType.EMPTY_FIELD
                )
            )
        }

        if (container.isNullOrEmpty()) {
            errors.add(
                ValidationError(
                    fieldId = containerId,
                    validationErrorType = ValidationErrorType.EMPTY_FIELD
                )
            )
        }

        if (products.isNullOrEmpty()) {
            errors.add(
                ValidationError(
                    fieldId = productsId,
                    validationErrorType = ValidationErrorType.EMPTY_FIELD
                )
            )
        }

        return errors
    }
}

data class RegisterStockValidatorParams(
    val reasonPair: Pair<Int, String?>,
    val containerPair: Pair<Int, String?>,
    val productsPair: Pair<Int, List<Product>?>
)