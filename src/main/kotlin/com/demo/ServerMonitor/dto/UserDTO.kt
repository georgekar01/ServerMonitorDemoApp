package com.demo.ServerMonitor.dto

import com.demo.ServerMonitor.enums.UserRole
import com.demo.ServerMonitor.models.User
import com.demo.ServerMonitor.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import kotlin.jvm.optionals.getOrNull

class UserDTO (
     val id: Long?,
     val username: String,
     val role : UserRole
)