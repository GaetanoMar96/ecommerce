package com.project.ecommerce.service;

import com.project.ecommerce.entity.User;
import com.project.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Mono<User> insertUser(User user) {
        return userRepository.insert(user)
            .flatMap(Mono::just)
            .onErrorResume(Mono::error);
    }

    public Mono<User> findByEmail(String email, String password) {
        return userRepository.findByEmail(email)
            .flatMap(user -> {
                if (!user.isEnabled())
                    return Mono.error(new UsernameNotFoundException("Account disabled."));

                if (!passwordEncoder.matches(password, user.getPassword()))
                    return Mono.error(new UsernameNotFoundException("Invalid user password!"));

                return Mono.just(user);
            })
            .switchIfEmpty(Mono.error(new UsernameNotFoundException("User not found")));
    }
}
