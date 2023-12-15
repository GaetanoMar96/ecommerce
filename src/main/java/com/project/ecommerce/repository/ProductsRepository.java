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
public class ProductsRepository implements CommonRepository {

    private final ReactiveMongoTemplate mongoTemplate;

    public Flux<Product> findProductsByGender(String gender) {
        Query query = getQueryByGender(gender);
        return postProcessProducts(mongoTemplate.find(query, Product.class));
    }

    public Flux<Product> findProductsBySearchQuery(String searchQuery) {
        Query query = getQueryBySearchQuery(searchQuery);
        return postProcessProducts(mongoTemplate.find(query, Product.class));
    }

    public Flux<Product> findProductsByDynamicFilter(ProductFilters productFilters) {
        Query query = getQueryByDynamicFilter(productFilters);
        return postProcessProducts(mongoTemplate.find(query, Product.class));
    }

    public Query getQueryByGender(String gender) {
        Query query = new Query();
        if (StringUtils.hasText(gender)) {
            query.addCriteria(Criteria.where("gender").is(gender));
        }
        return query;
    }

    public Query getQueryBySearchQuery(String searchQuery) {
        Query query = new Query();
        if (StringUtils.hasText(searchQuery)) {
            Criteria regexBrandCriteria = Criteria.where("brand").regex("^.*" + searchQuery + ".*$", "i");
            Criteria regexNameCriteria = Criteria.where("name").regex("^.*" + searchQuery + ".*$", "i");
            query.addCriteria(new Criteria().orOperator(regexBrandCriteria, regexNameCriteria));
        }
        return query;
    }

    public Query getQueryByDynamicFilter(ProductFilters productFilters) {
        Query query = getQueryByGender(productFilters.gender());
        if (productFilters.minPrice() != null && productFilters.maxPrice() != null) {
            query.addCriteria(Criteria.where("price").gte(productFilters.minPrice()).lte(productFilters.maxPrice()));
        } else if (productFilters.minPrice() != null) {
            query.addCriteria(Criteria.where("price").gte(productFilters.minPrice()));
        } else if (productFilters.maxPrice() != null) {
            query.addCriteria(Criteria.where("price").lte(productFilters.maxPrice()));
        }

        if (productFilters.brand() != null) {
            query.addCriteria(Criteria.where("brand").is(productFilters.brand()));
        }
        if (productFilters.colors() != null && !productFilters.colors().isEmpty()) {
            query.addCriteria(Criteria.where("images.color").in(productFilters.colors()));
        }
        return query;
    }
}
