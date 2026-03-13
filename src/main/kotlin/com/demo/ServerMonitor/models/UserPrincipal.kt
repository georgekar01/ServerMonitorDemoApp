package com.demo.ServerMonitor.models


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.Collections

//--------- A CLASS ABOUT THE LOGGED-IN PERSON
class UserPrincipal(
    private var user : User
) : UserDetails{

    fun UserPrincipal(user : User){
        this.user = user;
    }
    override fun getAuthorities(): Collection<GrantedAuthority> {
         return Collections.singletonList(SimpleGrantedAuthority("ROLE_${user.role}"))
    }

    override fun getPassword(): String {
        val password = user.password
        if(password == null){
            throw NullPointerException("User password is null!")
        }
        return user.password
    }

    override fun getUsername(): String {
        val username = user.username
        if(username == null){
            throw NullPointerException("User password is null!")
        }
        return username
    }

}