package com.mealmanager.backend.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
class SecurityConfig {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .authorizeHttpRequests { auth ->
                auth.requestMatchers("/api/v1/auth/**").permitAll()
                auth.requestMatchers("/api/v1/monitoring/health").permitAll()
                auth.anyRequest().authenticated()
            }
            .oauth2ResourceServer { oauth2 ->
                oauth2.jwt { jwt ->
                    jwt.jwtAuthenticationConverter(JwtAuthConverter())
                }
            }
        return http.build()
    }
}

class JwtAuthConverter :
    org.springframework.core.convert.converter.Converter<org.springframework.security.oauth2.jwt.Jwt, org.springframework.security.authentication.AbstractAuthenticationToken> {
    override fun convert(source: org.springframework.security.oauth2.jwt.Jwt): org.springframework.security.authentication.AbstractAuthenticationToken {
        return org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken(source)
    }
}

class JwtFilter : org.springframework.web.filter.OncePerRequestFilter {
    override fun doFilterInternal(
        request: jakarta.servlet.http.HttpServletRequest,
        response: jakarta.servlet.http.HttpServletResponse,
        filterChain: jakarta.servlet.FilterChain
    ) {
        filterChain.doFilter(request, response)
    }
}

class OAuth2Config {
    fun issuerUri(): String = "https://auth.mealmanager.com"
    fun clientId(): String = "meal-manager-client"
}

class BiometricConfig {
    fun enabled(): Boolean = true
    fun timeoutSeconds(): Int = 30
}
