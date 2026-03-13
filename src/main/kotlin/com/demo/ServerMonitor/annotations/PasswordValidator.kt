package com.demo.ServerMonitor.annotations

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class PasswordValidator : ConstraintValidator<Password, String> {
    override fun isValid(
        value: String?,
        context: ConstraintValidatorContext?
    ): Boolean {

        val passwordFormat = "^[a-z]@123\$".toRegex()

        if(value?.matches(passwordFormat) ?: return false){
            println("Validation successful")
            return true
        }
        println("Validation failed")
        return false
    }
}