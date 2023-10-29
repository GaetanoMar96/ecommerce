package com.project.ecommerce.repository;

import com.project.ecommerce.entity.Product;
import com.project.ecommerce.model.ProductFilters;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;

@Repository
@RequiredArgsConstructor
public class ProductsRepository  {

    private final ReactiveMongoTemplate mongoTemplate;

    public Flux<Product> findProductsByGenderAndCategory(String gender, String category) {
        Query query = getQueryByGenderAndCategory(gender, category);
        return mongoTemplate.find(query, Product.class);
    }

    public Query getQueryByGenderAndCategory(String gender, String category) {
        Query query = new Query();
        if (StringUtils.hasText(gender)) {
            query.addCriteria(Criteria.where("gender").is(gender));
        }
        if (StringUtils.hasText(category)) {
            query.addCriteria(Criteria.where("category").is(category));
        }
        return query;
    }

    public Flux<Product> findProductsByDynamicFilter(ProductFilters productFilters) {
        Query query = getQueryByGenderAndCategory(productFilters.gender(), productFilters.category());
        if (productFilters.minPrice() != null) {
            query.addCriteria(Criteria.where("price").gte(productFilters.minPrice()));
        }
        if (productFilters.maxPrice() != null) {
            query.addCriteria(Criteria.where("price").lte(productFilters.maxPrice()));
        }
        if (productFilters.color() != null) {
            query.addCriteria(Criteria.where("color").is(productFilters.color()));
        }
        return mongoTemplate.find(query, Product.class);
    }
}
