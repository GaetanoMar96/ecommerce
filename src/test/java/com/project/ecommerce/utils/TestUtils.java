package com.project.ecommerce.utils;

import com.project.ecommerce.entity.Product;
import java.util.Collections;
import org.springframework.core.io.ByteArrayResource;
import reactor.core.publisher.Flux;

public class TestUtils {

    public static Flux<Product> getProductsFromDb() {
        Product product1 = Product.builder()
            .id("1")
            .name("Product 1")
            .brand("Brand A")
            .description("Description 1")
            .price(100.0)
            .gender("Male")
            .build();

        Product product2 = Product.builder()
            .id("2")
            .name("Product 2")
            .brand("Brand B")
            .description("Description 2")
            .price(150.0)
            .gender("Female")
            .build();

        return Flux.just(product1, product2);
    }
}
