package com.project.ecommerce.service;

import com.project.ecommerce.entity.User;
import com.project.ecommerce.entity.User.Role;
import com.project.ecommerce.model.AuthRequest;
import com.project.ecommerce.model.AuthResponse;
import com.project.ecommerce.model.RegisterRequest;
import com.project.ecommerce.security.JwtService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;

    private final JwtService jwtService;

    private final PasswordEncoder passwordEncoder;

    public Mono<AuthResponse> register(RegisterRequest request) {
        User newUser = User.builder()
            .userId(UUID.randomUUID())
            .firstname(request.getFirstname())
            .lastname(request.getLastname())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .role(Role.USER)
            .build();

        return userService.insertUser(newUser)
            .map(user ->
                     AuthResponse.builder()
                         .userId(user.getUserId())
                         .firstname(user.getFirstname())
                         .lastname(user.getLastname())
                         .accessToken(jwtService.generateToken(user)).build()
            );
    }

    public Mono<AuthResponse> login(AuthRequest request) {
        return userService.findByEmail(request.getEmail(), request.getPassword())
            .map(user ->
                     AuthResponse.builder()
                         .userId(user.getUserId())
                         .accessToken(jwtService.generateToken(user)).build()
            );
    }
}
