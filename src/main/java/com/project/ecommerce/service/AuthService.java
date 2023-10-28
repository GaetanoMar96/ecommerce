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
        User user = User.builder()
            .userId(UUID.randomUUID())
            .firstname(request.getFirstname())
            .lastname(request.getLastname())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .role(Role.USER)
            .build();

        return userService.insertUser(user)
            .map(u -> AuthResponse.builder()
                .userId(u.getUserId())
                .firstname(u.getFirstname())
                .lastname(u.getLastname())
                .accessToken(jwtService.generateToken(u)).build());
    }

    public Mono<AuthResponse> login(AuthRequest request) {
        AuthResponse response = new AuthResponse();
        return userService.findByEmail(request.getEmail(), request.getPassword())
            .map(user -> {
                response.setUserId(user.getUserId());
                response.setAccessToken(jwtService.generateToken(user));
                return response;
            });
    }
}
