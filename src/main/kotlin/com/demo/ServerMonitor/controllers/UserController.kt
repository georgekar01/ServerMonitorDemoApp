package com.demo.ServerMonitor.controllers

import com.demo.ServerMonitor.dto.RegistrationDTO
import com.demo.ServerMonitor.dto.UserDTO
import com.demo.ServerMonitor.models.User
import com.demo.ServerMonitor.services.JwtService
import com.demo.ServerMonitor.services.RefreshTokenService
import com.demo.ServerMonitor.services.UserRegistrationService
import com.demo.ServerMonitor.services.UserService
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(
    @Autowired
    private val userRegistrationService: UserRegistrationService,

    @Autowired
    private val userService: UserService,

    @Autowired
    private val refreshTokenService: RefreshTokenService

) {

    @PostMapping("/register")
    fun registerUser(@Valid @RequestBody registrationDTO: RegistrationDTO): ResponseEntity<String> {

        try {
            userRegistrationService.register(registrationDTO)
            return ResponseEntity("Successfully registered", HttpStatus.CREATED)
        } catch (e: DataIntegrityViolationException) {
            return ResponseEntity("Username already exists", HttpStatus.CONFLICT)
        }

    }

    @PostMapping("/login")
    fun loginUser(@RequestBody user: User): ResponseEntity<String>{
        try {
            return ResponseEntity(userRegistrationService.verify(user), HttpStatus.OK)
        } catch (e: BadCredentialsException) {
            return ResponseEntity(HttpStatus.UNAUTHORIZED)
        }
    }

    @PostMapping("/refresh")
    fun refreshToken(@RequestBody payload : Map<String, String>) : ResponseEntity<String>{
        try {
            return ResponseEntity(refreshTokenService.refreshToken(payload), HttpStatus.OK)
        } catch (e: Exception) {
            return ResponseEntity(HttpStatus.UNAUTHORIZED)
        }
    }

    @GetMapping("/users")
    fun getAllUsers(): ResponseEntity<List<UserDTO>> {
        try {
            return ResponseEntity(userService.getAllUsers(), HttpStatus.OK)
        } catch (e: Exception) {
            return ResponseEntity(HttpStatus.OK)
        }
    }

}