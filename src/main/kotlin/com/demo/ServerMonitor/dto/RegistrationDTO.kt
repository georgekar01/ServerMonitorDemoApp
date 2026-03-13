package com.demo.ServerMonitor.dto

import com.demo.ServerMonitor.annotations.Password

class RegistrationDTO (
    val username: String,

    @Password
    val password: String,
)