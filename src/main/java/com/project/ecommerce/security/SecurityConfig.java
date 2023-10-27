package com.project.ecommerce.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

public class SecurityConfig {

    @Bean
    SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http,
        JwtService tokenProvider,
        ReactiveAuthenticationManager reactiveAuthenticationManager,
        ReactiveUserDetailsService userDetailsService) {

        return http.csrf(ServerHttpSecurity.CsrfSpec::disable)
            .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
            .authenticationManager(reactiveAuthenticationManager)
            .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
            .authorizeExchange(it->it
                .pathMatchers("auth").permitAll()
                .anyExchange().authenticated())
            .addFilterAt(new JwtTokenAuthenticationFilter(tokenProvider, userDetailsService), SecurityWebFiltersOrder.HTTP_BASIC)
            .build();
    }
}
