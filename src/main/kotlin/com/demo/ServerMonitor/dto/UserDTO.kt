package com.demo.ServerMonitor.dto

import com.demo.ServerMonitor.enums.UserRole
import com.demo.ServerMonitor.models.User

class UserDTO (
     val id: Long?,
     val username: String,
     val role : UserRole
)
