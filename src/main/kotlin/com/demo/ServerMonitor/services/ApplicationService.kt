package com.demo.ServerMonitor.services

import com.demo.ServerMonitor.enums.Status
import com.demo.ServerMonitor.models.Notification
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Repository

@Repository
interface ApplicationService {

    fun getNotifications(page : Int, size : Int, sortBy : String?) : Page<Notification>

    fun toggle(id : Long, status : Status) : Notification

    fun createNotification(notification : Notification) : String

    fun getByStatus(status: Status, page: Int, size : Int) : Page<Notification>

    fun deleteNotification(id: Long): List<Notification>

    fun updateNotification(notification: Notification) : List<Notification>

    //depreciated----- fun getAllNotifications() : List<Notification>
    //depreciated----- fun getByStatus(status : Status) : List<Notification>

}