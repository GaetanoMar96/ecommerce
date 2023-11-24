package com.project.ecommerce.controller;


import static org.mockito.Mockito.when;

import com.project.ecommerce.entity.User.Role;
import com.project.ecommerce.model.AuthRequest;
import com.project.ecommerce.model.AuthResponse;
import com.project.ecommerce.model.RegisterRequest;
import com.project.ecommerce.security.JwtService;
import com.project.ecommerce.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = AuthController.class, excludeAutoConfiguration = ReactiveSecurityAutoConfiguration.class)
class AuthControllerTest {

    private final String TEST_EMAIL = "test@email.com";

    private final String API = "/api/v1/ecommerce/auth";
    @Autowired
    private WebTestClient webClient;

    @MockBean
    AuthService authService;

    @MockBean
    JwtService jwtService;

    @Test
    void shouldSignUp() {
        AuthResponse responseDto = AuthResponse.builder()
            .accessToken("token")
            .build();

        RegisterRequest request = RegisterRequest.builder()
            .firstname("firstName")
            .lastname("lastName")
            .password("password")
            .email(TEST_EMAIL)
            .role(Role.USER)
            .build();

        when(authService.register(request)).thenReturn(Mono.just(responseDto));

        webClient
            .post().uri(API + "/register")
            .bodyValue(request)
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(AuthResponse.class);
    }

    @Test
    void shouldLogin() {
        AuthResponse responseDto = AuthResponse.builder()
            .accessToken("token")
            .build();

        AuthRequest request = new AuthRequest(TEST_EMAIL, "pwd");
        when(authService.login(request)).thenReturn(Mono.just(responseDto));

        webClient
            .post().uri(API + "/login")
            .bodyValue(request)
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(AuthResponse.class);
    }
}