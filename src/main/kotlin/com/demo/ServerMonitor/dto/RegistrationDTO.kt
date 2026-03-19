package com.demo.ServerMonitor.dto

import com.demo.ServerMonitor.annotations.Password
import com.demo.ServerMonitor.models.User
import org.springframework.stereotype.Component

class RegistrationDTO (
    val username: String,

    @Password
    val password: String,
){
    fun toEntity() : User{
        return User(
            username = this.username,
            password = this.password
        )
    }

}