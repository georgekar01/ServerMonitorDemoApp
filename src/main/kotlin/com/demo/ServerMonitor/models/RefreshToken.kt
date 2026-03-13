package com.demo.ServerMonitor.models

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne
import java.util.Date

@Entity
data class RefreshToken (

    @Id
    @GeneratedValue(GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne
    @JoinColumn("user_id", referencedColumnName = "id", unique = false, nullable = false)
    val user: User,

    @Column(nullable = false, unique = true)
    val token : String,

    @Column(nullable = false)
    val expiryDate : Date,

    @Column(nullable = false)
    var active : Boolean = true
)