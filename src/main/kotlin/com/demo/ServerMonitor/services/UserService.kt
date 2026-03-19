package com.demo.ServerMonitor.services

import com.demo.ServerMonitor.dto.UserDTO
import com.demo.ServerMonitor.models.User
import com.demo.ServerMonitor.models.UserPrincipal
import com.demo.ServerMonitor.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.crossstore.ChangeSetPersister
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UserService(

    @Autowired
    private val userRepository : UserRepository,

    ) : UserDetailsService {

    @Override
    override fun loadUserByUsername(username: String): UserDetails  {
        val user : User? = userRepository.findByUsername(username)

        if(user==null){
            throw Exception("User not found")
        }

        return UserPrincipal(user)
    }


    fun getAllUsers() : List<UserDTO>{
        val userList : List<User> =  userRepository.findAll()
        val userDTOList : MutableList<UserDTO> = mutableListOf()

        if(userList.isEmpty()){
            throw Exception("No users found")
        }

        for(user in userList){
            userDTOList.add(user.toDTO())
        }

        return userDTOList
    }

}