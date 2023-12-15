package com.project.ecommerce.controller;

import com.project.ecommerce.security.JwtService;
import com.project.ecommerce.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatusCode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static com.project.ecommerce.utils.TestUtils.getPayload;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = PaymentController.class, excludeAutoConfiguration = ReactiveSecurityAutoConfiguration.class)
public class PaymentControllerTest {

    private final String API = "/api/v1/ecommerce/payment";

    @Autowired
    private WebTestClient webClient;

    @MockBean
    PaymentService paymentService;

    @MockBean
    JwtService jwtService;

    @Test
    void processPayment_StatusOK() {
        when(paymentService.createPaymentIntent(getPayload())).thenReturn(Mono.just(HttpStatusCode.valueOf(200)));

        webClient
                .post().uri(API + "/process")
                .bodyValue(getPayload())
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void processPayment_StatusInternalServerError() {
        when(paymentService.createPaymentIntent(getPayload())).thenReturn(Mono.just(HttpStatusCode.valueOf(500)));

        webClient
                .post().uri(API + "/process")
                .bodyValue(getPayload())
                .exchange()
                .expectStatus()
                .is5xxServerError();
    }
}
