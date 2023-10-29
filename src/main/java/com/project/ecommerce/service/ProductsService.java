package com.project.ecommerce.service;

import com.project.ecommerce.entity.Product;
import com.project.ecommerce.model.ProductFilters;
import com.project.ecommerce.repository.ProductsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class ProductsService {

    private final ProductsRepository productsRepository;

    public Flux<Product> findProductsByGenderAndCategory(String gender, String category) {
        return productsRepository.findProductsByGenderAndCategory(gender, category);
    }

    public Flux<Product> findProductsByFilters(ProductFilters productFilters) {
        return productsRepository.findProductsByDynamicFilter(productFilters);
    }

}