package com.demo.ServerMonitor.annotations

import jakarta.validation.Constraint
import java.lang.annotation.RetentionPolicy
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Constraint(validatedBy = [PasswordValidator::class])
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class Password(
    val message: String = "",
    val groups: Array<KClass<out Any>> = [],
    val payload: Array<KClass<out Any>> = []
)
