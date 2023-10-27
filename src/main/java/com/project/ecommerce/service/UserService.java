package com.project.ecommerce.service;

import com.project.ecommerce.entity.User;
import com.project.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public Mono<Void> insertUser(User user) {
        return userRepository.insert(user)
            .onErrorResume(Mono::error)
            .then();
    }

    public Mono<User> findByUserId(String userId) {
        return userRepository.findById(userId)
            .switchIfEmpty(Mono.error(new UsernameNotFoundException("User not found")));
    }

    public Mono<User> findByEmail(String email) {
        return userRepository.findByEmail(email)
            .switchIfEmpty(Mono.error(new UsernameNotFoundException("User not found")));
    }
}
