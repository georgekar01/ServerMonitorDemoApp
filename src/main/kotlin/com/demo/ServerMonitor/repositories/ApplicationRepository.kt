package com.demo.ServerMonitor.repositories

import com.demo.ServerMonitor.enums.Status
import com.demo.ServerMonitor.models.Notification
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ApplicationRepository : JpaRepository<Notification, Long> {
    fun findByStatus(status : Status) : List<Notification>
    fun findByStatus(status: Status, pageable: Pageable): Page<Notification>
}