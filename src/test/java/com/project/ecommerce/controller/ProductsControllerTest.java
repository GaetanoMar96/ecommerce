package com.project.ecommerce.controller;

import static org.mockito.Mockito.when;

import com.project.ecommerce.model.ProductFilters;
import com.project.ecommerce.repository.ProductsRepository;
import com.project.ecommerce.security.JwtService;
import com.project.ecommerce.service.ProductsService;
import com.project.ecommerce.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Collections;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = ProductsController.class, excludeAutoConfiguration = ReactiveSecurityAutoConfiguration.class)
class ProductsControllerTest {

    private final String API = "/api/v1/ecommerce/products";
    @Autowired
    private WebTestClient webClient;

    @SpyBean
    ProductsService productsService;

    @SpyBean
    ProductsRepository productsRepository;

    @MockBean
    ReactiveMongoTemplate mongoTemplate;

    @MockBean
    JwtService jwtService;

    @Test
    void getAllProducts_StatusOK() {
        when(productsRepository.findAllProducts()).thenReturn(TestUtils.getProductsFromDb());
        when(productsService.findAllProducts()).thenReturn(TestUtils.getProductsFromDb());

        webClient
                .get().uri(API)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void getProductsByGender_StatusOK() {
        when(productsRepository.findProductsByGender("male")).thenReturn(TestUtils.getProductsFromDb());
        when(productsService.findProductsByGender("male")).thenReturn(TestUtils.getProductsFromDb());

        webClient
            .get().uri(API + "/{gender}", "male")
            .exchange()
            .expectStatus()
            .isOk();
    }

    @Test
    void getProductsBySearchQuery_StatusOK() {
        when(productsRepository.findProductsBySearchQuery("nike")).thenReturn(TestUtils.getProductsFromDb());
        when(productsService.findProductsBySearchQuery("nike")).thenReturn(TestUtils.getProductsFromDb());

        webClient
            .get().uri(API + "/search/{query}", "nike")
            .exchange()
            .expectStatus()
            .isOk();
    }

    @Test
    void getProductsByFilters_StatusOK() {
        ProductFilters filters = new ProductFilters("male", 0.00, 100.00, Collections.singleton("red"), "nike");
        when(productsRepository.findProductsByDynamicFilter(filters)).thenReturn(TestUtils.getProductsFromDb());
        when(productsService.findProductsByFilters(filters)).thenReturn(TestUtils.getProductsFromDb());

        webClient
            .post().uri(API + "/filters")
            .bodyValue(filters)
            .exchange()
            .expectStatus()
            .isOk();
    }

    @Test
    void getAllProducts_StatusKO() {
        when(productsRepository.findAllProducts()).thenThrow(RuntimeException.class);

        webClient
                .get().uri(API)
                .exchange()
                .expectStatus()
                .is5xxServerError();
    }

    @Test
    void getProductsByGender_StatusKO() {
        when(productsRepository.findProductsByGender("male")).thenThrow(RuntimeException.class);

        webClient
                .get().uri(API + "/{gender}", "male")
                .exchange()
                .expectStatus()
                .is5xxServerError();
    }

    @Test
    void getProductsBySearchQuery_StatusKO() {
        when(productsRepository.findProductsBySearchQuery("nike")).thenThrow(RuntimeException.class);

        webClient
                .get().uri(API + "/search/{query}", "nike")
                .exchange()
                .expectStatus()
                .is5xxServerError();
    }

    @Test
    void getProductsByFilters_StatusKO() {
        ProductFilters filters = new ProductFilters("male", 0.00, 100.00, Collections.singleton("red"), "nike");
        when(productsRepository.findProductsByDynamicFilter(filters)).thenThrow(RuntimeException.class);

        webClient
                .post().uri(API + "/filters")
                .bodyValue(filters)
                .exchange()
                .expectStatus()
                .is5xxServerError();
    }
}
