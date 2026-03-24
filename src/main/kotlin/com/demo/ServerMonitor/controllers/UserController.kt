package com.demo.ServerMonitor.controllers

import com.demo.ServerMonitor.dto.RegistrationDTO
import com.demo.ServerMonitor.dto.UserDTO
import com.demo.ServerMonitor.models.User
import com.demo.ServerMonitor.services.JwtService
import com.demo.ServerMonitor.services.RefreshTokenService
import com.demo.ServerMonitor.services.UserRegistrationService
import com.demo.ServerMonitor.services.UserService
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.apache.coyote.Request
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseCookie
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
    private val refreshTokenService: RefreshTokenService,

    @Value($$"${app.expirationTime}")
    private val expirationTime : Long,

    @Value($$"${app.refreshExpirationTime}")
    val refreshExpirationTime: Long
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
    fun loginUser(@RequestBody user : User,
                  response : HttpServletResponse ): ResponseEntity<Any>{
        try {

            val tokens = userRegistrationService.verify(user)
            val accessCookie = ResponseCookie.from("accessToken", tokens.get("accessToken"))
                .httpOnly(true)
                .secure(false)  //false for testing purpose (change it to true)
                .path("/")
                .maxAge(expirationTime/1000+600)  //cookie lives for 10 more minute than the jwt (if they expiration check is done whene they are close the active status does not update)
                .sameSite("Strict")
                .build()

            val refreshCookie = ResponseCookie.from("refreshToken", tokens.get("refreshToken"))
                .httpOnly(true)
                .secure(false)  //false for testing purpose (change it to true)
                .path("/")
                .maxAge(refreshExpirationTime/1000+600)  //cookie lives for 10 more minute than the jwt (if they expiration check is done whene they are close the active status does not update)
                .sameSite("Strict")
                .build()

            return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body("Logged in successfully")

        } catch (e: BadCredentialsException) {
            return ResponseEntity(HttpStatus.UNAUTHORIZED)
        }
    }

    @PostMapping("/logout")
    fun logout(request: HttpServletRequest) : ResponseEntity<String> {
        val clearAccessCookie = ResponseCookie.from("accessToken")
            .httpOnly(true)
            .secure(false)  //false for testing purpose (change it to true)
            .path("/")
            .maxAge(0)
            .sameSite("Strict")
            .build()

        val clearRefreshCookie = ResponseCookie.from("refreshToken")
            .httpOnly(true)
            .secure(false)  //false for testing purpose (change it to true)
            .path("/")
            .maxAge(0)
            .sameSite("Strict")
            .build()

        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, clearAccessCookie.toString())
            .header(HttpHeaders.SET_COOKIE, clearRefreshCookie.toString())
            .body("Logged out")

    }

    @PostMapping("/refresh")
    fun refreshToken(request: HttpServletRequest) : ResponseEntity<String>{
        try {

            //-----Fetching the existing accessToken cookie in order to modify it
            val cookie = request.cookies?.find{it.name == "refreshToken"}
            val token = refreshTokenService.refreshToken(cookie?.value ?: throw RuntimeException("Cookie not found"))

            val refreshedAccessCookie = ResponseCookie.from("accessToken", token)
                .httpOnly(true)
                .secure(false)  //false for testing purpose (change it to true)
                .path("/")
                .maxAge(expirationTime/1000+600)  //cookie lives for 10 more minute than the jwt (if they expiration check is done whene they are close the active status does not update)
                .sameSite("Strict")
                .build()

            return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshedAccessCookie.toString())
                .body("Token refreshed successfully")

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