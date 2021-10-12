package com.example.zebrascannerappdemo.domain.validator

import com.example.zebrascannerappdemo.domain.exception.ValidationError

interface Validator<in P> {
    fun validate(params: P): List<ValidationError>
}
