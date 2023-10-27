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

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;

    private final JwtService jwtService;

    private final PasswordEncoder passwordEncoder;

    public AuthResponse register(RegisterRequest request) {
        User user = User.builder()
            .userId(UUID.randomUUID())
            .firstname(request.getFirstname())
            .lastname(request.getLastname())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .role(Role.USER)
            .build();

        String jwtToken = jwtService.generateToken(user);
        userService.insertUser(user);

        return AuthResponse.builder()
            .userId(user.getUserId())
            .accessToken(jwtToken)
            .build();
    }

    public AuthResponse login(AuthRequest request) {
        /*authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
            )
        );*/
        AuthResponse response = new AuthResponse();
        userService.findByEmail(request.getEmail())
            .subscribe(user -> {
                if (user != null) {
                    String jwtToken = jwtService.generateToken(user);
                    response.setAccessToken(jwtToken);
                    response.setUserId(user.getUserId());
                }
            });
        return response;
    }
}
