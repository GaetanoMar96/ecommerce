package com.project.ecommerce.security.config;

import com.project.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    @Bean
    public ReactiveAuthenticationManager reactiveAuthenticationManager(ReactiveUserDetailsService userDetailsService) {
        var authenticationManager = new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
        authenticationManager.setPasswordEncoder(passwordEncoder());
        return authenticationManager;
    }

    /**
     * Custom implementaion of UserDetailsService
     * @param userRepository user repository
     * @return ReactiveUserDetailsService
     */
    @Bean
    public ReactiveUserDetailsService userDetailsService(UserRepository userRepository){
        return username-> userRepository.findByEmail(username)
            .map(u -> User
                .withUsername(u.getUsername()).password(u.getPassword())
                .authorities(u.getAuthorities().toArray(new GrantedAuthority[0]))
                .accountExpired(!u.isAccountNonExpired())
                .credentialsExpired(!u.isCredentialsNonExpired())
                .disabled(!u.isAccountNonLocked())
                .build()
            );
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
