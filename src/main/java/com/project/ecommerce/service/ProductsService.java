package com.project.ecommerce.service;

import com.project.ecommerce.entity.Product;
import com.project.ecommerce.model.ProductFilters;
import com.project.ecommerce.repository.ProductsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class ProductsService {

    private final ProductsRepository productsRepository;

    public Flux<Product> findAllProducts() {
        return productsRepository.findAllProducts();
    }

    public Flux<Product> findProductsByGender(String gender) {
        return productsRepository.findProductsByGender(gender);
    }

    public Flux<Product> findProductsBySearchQuery(String query) {
        return productsRepository.findProductsBySearchQuery(query);
    }

    public Flux<Product> findProductsByFilters(ProductFilters productFilters) {
        return productsRepository.findProductsByDynamicFilter(productFilters);
    }

}
