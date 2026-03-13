package com.demo.ServerMonitor.repositories

import com.demo.ServerMonitor.models.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    fun findByUsername(username: String): User?
}