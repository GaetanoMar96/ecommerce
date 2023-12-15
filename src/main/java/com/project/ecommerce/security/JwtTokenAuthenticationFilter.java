package com.project.ecommerce.security;

import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
@RequiredArgsConstructor
public class JwtTokenAuthenticationFilter implements WebFilter {

    private final JwtService jwtService;

    private final ReactiveUserDetailsService userDetailsService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        if (exchange.getRequest().getURI().getPath().contains("/api/v1/ecommerce/auth")) {
            return chain.filter(exchange);
        }

        String token = resolveToken(exchange.getRequest());
        String userEmail = jwtService.extractUsername(token);
        if (StringUtils.hasText(token) && this.jwtService.isTokenValid(token, userEmail)) {
            return Mono.fromCallable(() -> getAuthentication(token, userEmail))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(authentication -> chain.filter(exchange)
                    .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication)));
        }
        return chain.filter(exchange);
    }

    public Authentication getAuthentication(String token, String userEmail) {
        Object authoritiesClaim = jwtService.extractAuthorities(token);

        Collection<? extends GrantedAuthority> authorities = authoritiesClaim == null
            ? AuthorityUtils.NO_AUTHORITIES
            : AuthorityUtils
                .commaSeparatedStringToAuthorityList(authoritiesClaim.toString());

        return userDetailsService.findByUsername(userEmail)
            .map(userDetails -> new UsernamePasswordAuthenticationToken(userDetails, null, authorities))
            .block();
    }

    private String resolveToken(ServerHttpRequest request) {
        String bearerToken = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        return StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ") ? bearerToken.substring(7) : null;
    }
}