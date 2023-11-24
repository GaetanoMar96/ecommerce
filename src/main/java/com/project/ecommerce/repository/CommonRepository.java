package com.project.ecommerce.repository;

import com.project.ecommerce.entity.Product;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Base64;

public interface CommonRepository {

    default Flux<Product> postProcessProducts(Flux<Product> products) {
        if (products == null) {
            return Flux.empty();
        }
        Base64.Decoder decoder = Base64.getDecoder();
        return products.flatMap(product -> {
            if(!CollectionUtils.isEmpty(product.getImages())) {
                product.getImages().forEach(image -> image.setImageBin(decoder.decode(image.getImage())));
            }
            return Mono.just(product);
        });
    }
}
