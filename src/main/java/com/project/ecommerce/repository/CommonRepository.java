package com.project.ecommerce.repository;

import com.project.ecommerce.entity.Product;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface CommonRepository {

    default Flux<Product> postProcessProducts(Flux<Product> products) {
        if (products == null) {
            return Flux.empty();
        }
        return products.flatMap(product -> {
            if(!CollectionUtils.isEmpty(product.getImages())) {
                //Temporary solution
                product.getImages().forEach(image -> image.setImage(
                        product.getBrand() + "-" + product.getName() + "-" + image.getColor() + ".png"));
            }
            return Mono.just(product);
        });
    }
}
