package com.project.ecommerce.service;

import static org.mockito.Mockito.when;

import com.project.ecommerce.entity.User;
import com.project.ecommerce.entity.User.Role;
import com.project.ecommerce.repository.UserRepository;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;
    private final String TEST_EMAIL = "test@email.com";

    @Test
    void insertUser() {
        User user = User.builder()
            .userId(UUID.randomUUID())
            .firstname("firstName")
            .lastname("lastName")
            .email(TEST_EMAIL)
            .role(Role.USER)
            .password("password")
            .build();

        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Mono.just(user));
        when(userRepository.insert(user)).thenReturn(Mono.just(user));
        Mono<User> userMono = userService.insertUser(user);
        StepVerifier
            .create(userMono)
            .consumeNextWith(newUser -> Assertions.assertEquals(newUser.getEmail(), TEST_EMAIL))
            .verifyComplete();
    }

    @Test
    void userNotFound() {
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Mono.empty());
        Mono<User> userMono = userService.findByEmail(TEST_EMAIL, "password");
        StepVerifier
            .create(userMono)
            .expectErrorMatches(throwable -> throwable instanceof UsernameNotFoundException)
            .verify();
    }
}