package com.demo.ServerMonitor.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfig(
    @Autowired
    private val userDetailsService : UserDetailsService,

    @Autowired
    private val jwtFilter : JwtFilter,

    @Value($$"${app.encryptionRounds}")
    private val encryptionRounds: Int

){
    @Bean
    fun securityFilterChain(http : HttpSecurity) : SecurityFilterChain{

        return http
                    .csrf{customizer -> customizer.disable()}
                    .authorizeHttpRequests { request -> request
                        .requestMatchers("/register","/login", "/refresh", "/users","/index.html", "/ws/**") //if i dont type the mappings correctly the program gives a brutal error
                        .permitAll()
                        .anyRequest()
                        .authenticated() }
                    //.formLogin(Customizer.withDefaults())
                    //.httpBasic(Customizer.withDefaults())
                    //.oauth2Login(Customizer.withDefaults())
                    //.sessionManagement{session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)}
                    .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter::class.java)
                    .build()

    }


    @Bean
    fun authenticationProvider() : AuthenticationProvider {
        val provider : DaoAuthenticationProvider = DaoAuthenticationProvider(userDetailsService)
        provider.setPasswordEncoder(BCryptPasswordEncoder(encryptionRounds))  //the decryption happens hear
        return provider
    }

    @Bean
    fun authenticationManager(config : AuthenticationConfiguration) : AuthenticationManager {   //makes a custom authManager which selects
        return config.authenticationManager                                                     //which authProvider to call and then implement
    }                                                                                           //authentication (so it connects the two entities)

}

