package com.demo.ServerMonitor.services

import com.demo.ServerMonitor.dto.RegistrationDTO
import com.demo.ServerMonitor.dto.RegistrationMapper
import com.demo.ServerMonitor.models.User
import com.demo.ServerMonitor.repositories.RefreshTokenRepository
import com.demo.ServerMonitor.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserRegistrationService (
    @Autowired
    private val usersRepository : UserRepository,

    @Autowired
    private val authenticationManager : AuthenticationManager,

    @Autowired
    private val jwtService : JwtService,

    @Autowired
    private val registrationMapper: RegistrationMapper,

    @Value($$"${app.encryptionRounds}")
    private val encryptionRounds: Int,

    @Autowired
    private val refreshTokenService: RefreshTokenService


){

    @Autowired
    private lateinit var refreshTokenRepository: RefreshTokenRepository
    private val encoder : BCryptPasswordEncoder = BCryptPasswordEncoder(encryptionRounds)    //the encryption happens hear

    fun register(registrationDTO: RegistrationDTO){

        if(usersRepository.findByUsername(registrationDTO.username)!=null){
            throw BadCredentialsException("Username not found")
        }

        val user : User = registrationMapper.toEntity(registrationDTO)
        user.password = encoder.encode(user.password).toString()   //this makes the problem on validation
        usersRepository.save(user)                                        //created registrationDTO to use for raw validation (not encrypted)
    }

    fun verify(user : User) : String{
        val authenticated : Authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                user.username,
                user.password
            )
        )

        if(authenticated.isAuthenticated()){
            val accessToken = jwtService.generateToken(user.username, user.role)
            val refreshToken = refreshTokenService.createRefreshToken(user.username).token

            //return mapOf("accessToken" to accessToken,"refreshToken" to refreshToken)
            return accessToken
        }
        throw BadCredentialsException("Authentication failed")
    }








}