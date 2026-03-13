package com.demo.ServerMonitor.annotations

import jakarta.validation.Constraint
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [MessageValidator::class])
@MustBeDocumented
annotation class Message(
    val message: String = "Message must be valid",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Any>> = []
)
