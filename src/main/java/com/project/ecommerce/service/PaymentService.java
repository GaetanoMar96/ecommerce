package com.project.ecommerce.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentService {

    @Value("${stripe.secretKey}")
    private String secretKey;

    private final WebClient webClient = WebClient.create("https://api.stripe.com/v1");

    public Mono<HttpStatusCode> createPaymentIntent(Map<String, String> payload) {
        Stripe.apiKey = secretKey;
        String paymentMethodId = payload.get("paymentMethodId");
        String amount = payload.get("amount");
        return webClient.post()
                .uri("/payment_intents")
                .header("Authorization", "Bearer " + secretKey)
                .body(BodyInserters.fromFormData("payment_method", paymentMethodId)
                        .with("currency", "eur")
                        .with("amount", amount))
                .exchangeToMono(
                        response -> {
                            if (response.statusCode().is2xxSuccessful()) {
                                return Mono.just(response.statusCode());
                            } else {
                                return response.bodyToMono(String.class).flatMap(errorBody -> Mono.error(new RuntimeException("HTTP error: " + response.statusCode() + ", Body: " + errorBody)));
                            }
                        })
                .onErrorMap(this::handleStripeException);
    }

    private Throwable handleStripeException(Throwable throwable) {
        if (throwable instanceof StripeException stripeException) {
            return new RuntimeException(stripeException.getMessage());
        }
        return throwable;
    }
}
