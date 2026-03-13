package com.demo.ServerMonitor.repositories

import com.demo.ServerMonitor.models.RefreshToken
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Date

interface RefreshTokenRepository : JpaRepository<RefreshToken, Long> {
    fun findByToken(token : String) : RefreshToken
}