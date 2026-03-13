package com.demo.ServerMonitor.dto

import com.demo.ServerMonitor.models.User
import org.springframework.stereotype.Component

@Component
class UserMapper {
    fun toDTO(user : User) : UserDTO{
        return UserDTO(
            id = user.id,
            username = user.username,
            role = user.role
        )
    }
}