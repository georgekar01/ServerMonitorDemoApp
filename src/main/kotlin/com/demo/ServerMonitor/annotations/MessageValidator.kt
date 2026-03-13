package com.demo.ServerMonitor.annotations

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class MessageValidator : ConstraintValidator<Message, String> {
    override fun isValid(
        value: String?,
        context: ConstraintValidatorContext?
    ): Boolean {
        val regex = Regex("^notification \\d+\$")

        if(value==null){
            return false
        }
        return value.matches(regex)

    }
}