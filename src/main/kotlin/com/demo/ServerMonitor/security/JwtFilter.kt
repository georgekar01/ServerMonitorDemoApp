package com.demo.ServerMonitor.security

import com.demo.ServerMonitor.services.JwtService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtFilter(
    @Autowired
    private val jwtService: JwtService,

    @Autowired
    private val userDetailsService: UserDetailsService

) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {

        //-----Fetching the existing accessToken cookie in order to add it to the filtering
        val token = request.cookies?.find{it.name == "accessToken"}?.value

        if(token==null){
            filterChain.doFilter(request, response)
            return
        }

        val username = try {
            jwtService.extractUsername(token)
        } catch (e: Exception) {
            null
        }

        if(username!=null && SecurityContextHolder.getContext().authentication == null) {
                val userDetails : UserDetails = userDetailsService.loadUserByUsername(username)

                if(jwtService.validateToken(token, userDetails)) {
                    val authToken = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)

                    authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                    SecurityContextHolder.getContext().authentication = authToken
                }

            }

        filterChain.doFilter(request,response)

    }

}
