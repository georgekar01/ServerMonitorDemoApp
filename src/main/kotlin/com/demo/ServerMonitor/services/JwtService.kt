package com.demo.ServerMonitor.services

import com.demo.ServerMonitor.enums.UserRole
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.keygen.BytesKeyGenerator
import org.springframework.stereotype.Service
import java.security.Key
import java.security.NoSuchAlgorithmException
import java.util.*
import javax.crypto.SecretKey
//import kotlin.io.encoding.Base64
import java.util.Base64
import javax.crypto.KeyGenerator

@Service
class JwtService (

    @Value($$"${app.expirationTime}")
    private val expirationTime : Long,

    @Value($$"${app.keyGenAlgorithm}")
    private val keyGenAlgorithm: String

){
    var secretKey : String = ""

    init{
        try {
            val keyGenerator : KeyGenerator  = KeyGenerator.getInstance(keyGenAlgorithm)
            val sk : SecretKey = keyGenerator.generateKey()
            secretKey = Base64.getEncoder().encodeToString(sk.getEncoded())
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException(e)
        }

    }

    fun generateToken(username : String, role : UserRole) : String{

        val now = Date()
        val expiryDate = Date(now.time+expirationTime)   //plus 1 hour
        return Jwts.builder()
            .subject(username)
            .claim("role",role)
            .issuedAt(now)
            .expiration(expiryDate)
            .signWith(getSigningKey())  //encrypted key
            .compact()

    }

    private fun getSigningKey(): SecretKey {
        val keyBytes : ByteArray  = Base64.getDecoder().decode(secretKey)
        return Keys.hmacShaKeyFor(keyBytes)
    }

    fun extractAllClaims(token : String) : Claims {
        return Jwts.parser()
            .verifyWith(getSigningKey())
            .build()
            .parseSignedClaims(token)
            .payload
    }

    fun extractUsername(token : String) : String{
        return extractAllClaims(token).subject
    }

    fun isTokenExpired(token : String) : Boolean{
        return extractAllClaims(token).expiration.before(Date())
    }

    fun validateToken(token : String, userDetails : UserDetails) : Boolean{
        val username = extractUsername(token)
        return username == userDetails.username && isTokenExpired(token)==false
    }

}