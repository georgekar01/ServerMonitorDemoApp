package com.demo.ServerMonitor.services

import com.demo.ServerMonitor.models.RefreshToken
import com.demo.ServerMonitor.repositories.RefreshTokenRepository
import com.demo.ServerMonitor.repositories.UserRepository
import org.hibernate.internal.util.PropertiesHelper.map
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.util.Date
import java.util.UUID

@Service
class RefreshTokenService (
    @Value($$"${app.refreshExpirationTime}")
    val refreshExpirationTime: Long,

    @Autowired
    private val refreshTokenRepository: RefreshTokenRepository,

    @Autowired
    private val userRepository: UserRepository,

    @Autowired
    private var jwtService: JwtService

){

    fun createRefreshToken(username : String, expiration : Long) : RefreshToken{
        val now = Date()
        val expirationDate = Date(now.time+expiration)

        val userToRefresh = userRepository.findByUsername(username) ?: throw RuntimeException("User not found")

        return refreshTokenRepository.save(RefreshToken(
                user = userToRefresh,
                //user = userRepository.findByUsername(username) ?: throw RuntimeException("User not found"),   //returns User value instead of optional
                //token = UUID.randomUUID().toString(),
                token = jwtService.generateToken(userToRefresh.username, userToRefresh.role, expiration),
                expiryDate = expirationDate
            )
        )
    }

    fun refreshToken(payload : String) : String {

        val requestToken : String = payload

        if(requestToken.isNullOrBlank()){
            throw RuntimeException("Refresh token is required")
        }

        val token = refreshTokenRepository.findByToken(requestToken)

        if(jwtService.isTokenExpired(payload)){
            token.active = false
            refreshTokenRepository.saveAndFlush(token)
            throw RuntimeException("Refresh token is expired. Please login again")
        }

        val tokenGenerated  = jwtService.generateToken(
            token.user.username,
            token.user.role
        )

        println("New jwt: "+tokenGenerated)
        return tokenGenerated

    }

}
