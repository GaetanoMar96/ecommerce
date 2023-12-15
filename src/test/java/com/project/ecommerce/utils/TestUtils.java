package com.project.ecommerce.utils;

import com.project.ecommerce.entity.Product;
import reactor.core.publisher.Flux;

import java.util.Collections;
import java.util.Map;

public class TestUtils {

    public static Flux<Product> getProductsFromDb() {
        Product product1 = Product.builder()
            .id("1")
            .name("Product 1")
            .brand("Brand A")
            .description("Description 1")
            .price(100.0)
            .gender("Male")
                .images(Collections.singletonList(new Product.ImageData("64582hdh7w", null, "blue")))
            .build();

        Product product2 = Product.builder()
            .id("2")
            .name("Product 2")
            .brand("Brand B")
            .description("Description 2")
            .price(150.0)
            .gender("Female")
                .images(Collections.singletonList(new Product.ImageData("64582hdh7w", null, "red")))
            .build();

        return Flux.just(product1, product2);
    }

    public static Map<String, String> getPayload() {
        return Map.of(
                "paymentMethodId", "pm_visa_card",
                "amount", "100",
                "currency", "eur"
        );
    }
}
