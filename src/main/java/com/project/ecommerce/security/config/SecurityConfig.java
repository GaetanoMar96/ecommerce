package com.project.ecommerce.security.config;

import com.project.ecommerce.security.JwtService;
import com.project.ecommerce.security.JwtTokenAuthenticationFilter;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.logout.LogoutWebFilter;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.security.web.server.csrf.CookieServerCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http,
        JwtService tokenProvider,
        ReactiveUserDetailsService userDetailsService,
        ReactiveAuthenticationManager reactiveAuthenticationManager) {

        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable) //temporaneo
            //.csrf(csrfSpec -> csrfSpec.csrfTokenRepository(new CookieServerCsrfTokenRepository()))
            .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
            .cors(corsSpec -> corsSpec.configurationSource(corsConfiguration()))
            .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
            .authenticationManager(reactiveAuthenticationManager)
            .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
            .authorizeExchange(exchanges -> exchanges
                .pathMatchers("/api/v1/ecommerce/auth/register").permitAll()
                .pathMatchers("/api/v1/ecommerce/auth/login").permitAll()
                .anyExchange().permitAll()) //To be changed
            .addFilterAt(new JwtTokenAuthenticationFilter(tokenProvider, userDetailsService), SecurityWebFiltersOrder.AUTHENTICATION)
            .addFilterAt(logoutFilter(), SecurityWebFiltersOrder.LOGOUT)
            .build();
    }

    @Bean
    CorsConfigurationSource corsConfiguration() {
        var corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(Collections.singletonList("*"));
        corsConfiguration.setAllowedMethods(Collections.singletonList("*"));
        corsConfiguration.setAllowedHeaders(Collections.singletonList("*"));
        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    @Bean
    LogoutWebFilter logoutFilter() {
        return new LogoutWebFilter();
    }
}
