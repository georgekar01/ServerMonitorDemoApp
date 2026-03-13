package com.demo.ServerMonitor.services

import com.demo.ServerMonitor.enums.Status
import com.demo.ServerMonitor.models.Notification
import com.demo.ServerMonitor.repositories.ApplicationRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.Optional
import java.util.spi.ToolProvider.findFirst

@Service
class ApplicationServiceImpl(
    private val applicationRepository : ApplicationRepository,
    private val messagingTemplate: SimpMessagingTemplate
) : ApplicationService{

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERADMIN')")
    @Override
    override fun createNotification(notification: Notification) : String {
        //notification.id = nextId++;
        //notification.id=null
        applicationRepository.save(notification)
        messagingTemplate.convertAndSend("/topic/notifications", notification)    //websocket affiliated
        return "Successfully created new notification"
    }

    /* ----------------------Depreciated
    @PreAuthorize("hasAnyRole('ADMIN','USER','SUPERADMIN')")
    @Override
    override  fun getAllNotifications() : List<Notification> {
        val notifications : List<Notification> = applicationRepository.findAll();
        return notifications;
    }
     */

    @PreAuthorize("hasAnyRole('ADMIN','USER','SUPERADMIN')")
    @Override
    override fun getNotifications(page : Int, size : Int, sortBy : String?) : Page<Notification>{
        val sort = Sort.by(sortBy ?: "status").descending() //by default having status sorting (elvis operator)
        val pageable = PageRequest.of(page, size, sort)
        return applicationRepository.findAll(pageable)
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER','SUPERADMIN')")
    @Override
    override fun getByStatus(status: Status, page: Int, size : Int): Page<Notification> {
        val pageable = PageRequest.of(page, size)
        val result = applicationRepository.findByStatus(status, pageable)

        if(!result.isEmpty){
            return result
        }
        throw ResponseStatusException(HttpStatus.NOT_FOUND, "No Notifications with "+status+" were found")
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    override fun deleteNotification(id: Long) : List<Notification> {
        // 1. Perform DB Deletion
        applicationRepository.deleteById(id)
        // 2. Efficiently Notify Clients
        val payload : Any = mapOf(
            "id" to id,
            "action" to "DELETE"
        )

        // This ensures all clients (not just the one who clicked delete)
        // receive the instruction to remove ID from their feed.
        messagingTemplate.convertAndSend("/topic/notifications", payload)
        return applicationRepository.findAll()   //useless(just for return purposes to not change the interface)
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
    @Override
    override fun updateNotification(notification: Notification): List<Notification> {
        val repoList : List<Notification> = applicationRepository.findAll()
        val toUpdate : Notification? =  repoList.find{it.id==notification.id}

        if(toUpdate==null){
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "No Notification with " + notification.id + " was found")
        }

        toUpdate.database_id = notification.database_id
        toUpdate.status = notification.status
        toUpdate.message = notification.message
        toUpdate.timeStamp = notification.timeStamp

        applicationRepository.save(toUpdate)
        messagingTemplate.convertAndSend("/topic/notifications", toUpdate)    //websocket affiliated
        return  repoList
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
    @Override
    override fun toggle(id : Long, status : Status): Notification {
        val repoList : List<Notification> = applicationRepository.findAll()
        val toUpdate : Notification? =  repoList.find{it.id==id}

        if(toUpdate==null){
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "No Notification with " + id + " was found")
        }

        toUpdate.status = status
        applicationRepository.save(toUpdate)
        messagingTemplate.convertAndSend("/topic/notifications", toUpdate)   //websocket affiliated
        return toUpdate
    }

    /*
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    override fun deleteNotification(id: Long): List<Notification> {

        val before: List<Notification> = applicationRepository.findAll()

        applicationRepository.deleteById(id)

        val after: List<Notification> = applicationRepository.findAll()

        messagingTemplate.convertAndSend("/topic/notifications", after)    //websocket affiliated
        //bug-------------------


        if (after.equals(before) == false) {
            return after;
        }
        throw ResponseStatusException(HttpStatus.NOT_FOUND, "No Notification with " + id + " was found to delete")
    }
    */

}