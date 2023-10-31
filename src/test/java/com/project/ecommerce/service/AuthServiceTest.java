package com.project.ecommerce.service;

import static org.mockito.Mockito.when;

import com.project.ecommerce.entity.User;
import com.project.ecommerce.entity.User.Role;
import com.project.ecommerce.model.AuthRequest;
import com.project.ecommerce.model.AuthResponse;
import com.project.ecommerce.model.RegisterRequest;
import com.project.ecommerce.repository.UserRepository;
import com.project.ecommerce.security.JwtService;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
class AuthServiceTest {

    private final String TEST_EMAIL = "test@email.com";

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserService userService;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder pwdEncoder;

    @Mock
    private UserRepository userRepository;

    @Test
    void shouldRegister() {
        RegisterRequest request = RegisterRequest.builder()
            .firstname("firstName")
            .lastname("lastName")
            .password("password")
            .email(TEST_EMAIL)
            .role(Role.USER)
            .build();

        User user = User.builder()
            .userId(UUID.randomUUID())
            .firstname("firstName")
            .lastname("lastName")
            .password("hashedpwd")
            .email(TEST_EMAIL)
            .role(Role.USER)
            .build();

        when(userRepository.insert(new User(Mockito.any(), "firstName", "lastName", TEST_EMAIL, "hashedpwd", Role.USER))).thenReturn(Mono.just(user));
        when(userService.insertUser(new User(Mockito.any(), "firstName", "lastName", TEST_EMAIL, "hashedpwd", Role.USER))).thenReturn(Mono.just(user));
        when(jwtService.generateToken(Mockito.any())).thenReturn("token");
        when(pwdEncoder.encode("password")).thenReturn("hashedpwd");
        Mono<AuthResponse> response = authService.register(request);
        StepVerifier
            .create(response)
            .consumeNextWith(r -> Assertions.assertEquals("token", r.getAccessToken()))
            .verifyComplete();
    }

    @Test
    void shouldLogin() {
        User user = User.builder()
            .firstname("firstName")
            .lastname("lastName")
            .password("password")
            .email(TEST_EMAIL)
            .role(Role.USER)
            .build();

        AuthRequest request = new AuthRequest(TEST_EMAIL, "password");
        when(userService.findByEmail(TEST_EMAIL, "password")).thenReturn(Mono.just(user));
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Mono.just(user));
        when(jwtService.generateToken(Mockito.any())).thenReturn("token");
        Mono<AuthResponse> response = authService.login(request);

        StepVerifier
            .create(response)
            .consumeNextWith(r -> Assertions.assertEquals("token", r.getAccessToken()))
            .verifyComplete();
    }
}
