package com.demo.ServerMonitor.models

import com.demo.ServerMonitor.annotations.Message
import com.demo.ServerMonitor.enums.Status
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import java.time.LocalDateTime


@Entity(name="notifications")
 data class Notification (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     var id : Long? = null,

    @Column(nullable = false)
    var database_id : Long,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status : Status,

    @Message                        //--------custom message format validator
    @Column(nullable = true)
    var message : String,

    @Column(nullable = false)
    var timeStamp : LocalDateTime){

 }