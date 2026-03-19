package com.demo.ServerMonitor.models


import com.demo.ServerMonitor.annotations.Message
import com.demo.ServerMonitor.annotations.Password
import com.demo.ServerMonitor.dto.UserDTO
import com.demo.ServerMonitor.enums.UserRole
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity(name="users")
data class User (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id : Long? = null,

    @Column(nullable = false)
    var username: String,

    @Column(nullable = false)
    var password: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var role : UserRole = UserRole.USER

){
    fun toDTO() : UserDTO{
        return UserDTO(
            id = this.id,
            username = this.username,
            role = this.role
        )
    }

}

