package com.demo.ServerMonitor.controllers

import com.demo.ServerMonitor.enums.Status
import com.demo.ServerMonitor.models.Notification
import com.demo.ServerMonitor.services.ApplicationService
import com.demo.ServerMonitor.services.ApplicationServiceImpl
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.security.web.csrf.CsrfToken
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.security.Principal

@RestController
@Validated
@RequestMapping("/api/notifications")
class ApplicationController (
    @Autowired
    private val applicationService : ApplicationService

    ){

    /* ----------------------Depreciated
    @GetMapping("/logs")
    fun getAllNotifications() : ResponseEntity<List<Notification>>{
        return ResponseEntity(applicationService.getAllNotifications(), HttpStatus.OK)
    }
     */

    @GetMapping("/logs")
    fun getNotifications(
        @RequestParam @Min(0) page : Int,
        @RequestParam @Min(0) @Max(50) size : Int,
        @RequestParam(required = false) sortBy : String?         //We can add validation here
    ) : ResponseEntity<Page<Notification>>{
        return ResponseEntity(applicationService.getNotifications(page, size, sortBy), HttpStatus.OK)
    }


    @PostMapping("/notify")
    fun createNotification(@Valid @RequestBody notification : Notification) : ResponseEntity<String>{
        return ResponseEntity(applicationService.createNotification(notification), HttpStatus.OK)
    }


    @GetMapping("/status/{status}")
    fun getByStatus(@PathVariable status : Status,
                    principal: Principal,    //-----------Why is it here?????
                    @RequestParam @Min(0) page: Int,
                    @RequestParam @Min(0) @Max(50) size: Int
    ) : ResponseEntity<Page<Notification>>{

        try{
            val result = applicationService.getByStatus(status, page, size)
            return ResponseEntity(result,HttpStatus.OK)
        }catch(e : ResponseStatusException){
            return ResponseEntity(e.getStatusCode())
        }

    }

    @DeleteMapping("/drop/{id}")    //We can add validation here
    fun deleteNotification(@PathVariable @Min(0, message = "@@@@@@@@@") id : Long
    ) : ResponseEntity<List<Notification>> {
        try{
            return ResponseEntity(applicationService.deleteNotification(id), HttpStatus.OK)
        }catch(e : ResponseStatusException){
            return ResponseEntity(e.getStatusCode())
        }
    }

    @PutMapping("/{id}/{status}")    //We can add validation here
    fun toggleNotification(@PathVariable @Min(0) id : Long,
                           @PathVariable status : Status
    ) : ResponseEntity<Notification>{
            try{
                return ResponseEntity(applicationService.toggle(id, status), HttpStatus.OK)
            }catch(e : ResponseStatusException){
                return ResponseEntity(e.getStatusCode())
            }
    }

    @PutMapping("/notification")
    fun updateNotification(@Valid @RequestBody notification : Notification): ResponseEntity<List<Notification>>{
        try{
            return ResponseEntity(applicationService.updateNotification(notification), HttpStatus.OK)
        }catch(e : ResponseStatusException){
            return ResponseEntity(e.getStatusCode())
        }
    }

    @GetMapping("/csrf-token")
    fun getCsrfToken(request : HttpServletRequest) : CsrfToken{
        return  request.getAttribute("_csrf") as CsrfToken
    }


}