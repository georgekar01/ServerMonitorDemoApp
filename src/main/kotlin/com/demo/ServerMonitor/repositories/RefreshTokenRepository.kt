package com.demo.ServerMonitor.repositories

import com.demo.ServerMonitor.models.RefreshToken
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Date

@Repository
interface RefreshTokenRepository : JpaRepository<RefreshToken, Long> {
    fun findByToken(token : String) : RefreshToken
}