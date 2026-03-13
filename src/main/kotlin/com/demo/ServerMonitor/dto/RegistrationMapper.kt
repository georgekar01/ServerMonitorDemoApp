package com.demo.ServerMonitor.dto

import com.demo.ServerMonitor.models.User
import org.springframework.stereotype.Component

@Component
class RegistrationMapper {

    fun toDTO(user : User) : RegistrationDTO{
        return RegistrationDTO(
            username = user.username,
            password = user.password,
        )
    }

    fun toEntity(registrationDTO: RegistrationDTO) : User {
        return User(
            username = registrationDTO.username,
            password = registrationDTO.password
        )
    }

}