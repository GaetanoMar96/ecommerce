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

    public Flux<Product> findProductsByGender(String gender) {
        Query query = getQueryByGender(gender);
        return mongoTemplate.find(query, Product.class);
    }

    public Flux<Product> findProductsBySearchQuery(String searchQuery) {
        Query query = getQueryBySearchQuery(searchQuery);
        return mongoTemplate.find(query, Product.class);
    }

    public Flux<Product> findProductsByDynamicFilter(ProductFilters productFilters) {
        Query query = getQueryByDynamicFilter(productFilters);
        Flux<Product> products = mongoTemplate.find(query, Product.class);
        String color = productFilters.color();
        if (!StringUtils.hasText(color)) {
            return products;
        }
        //get only images by desired color
        return products
            .flatMap(product -> {
                product.getImages().removeIf(image -> !color.equals(image.getColor()));
                if (!product.getImages().isEmpty()) {
                    return Flux.just(product);
                }
                return Flux.empty();
            })
            .filter(product -> !product.getImages().isEmpty());
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
            Criteria regexBrandCriteria = Criteria.where("brand").regex("^.*" + query + ".*$", "i");
            Criteria regexNameCriteria = Criteria.where("name").regex("^.*" + query + ".*$", "i");
            query.addCriteria(regexNameCriteria);
            query.addCriteria(regexBrandCriteria);
        }
        return query;
    }

    public Query getQueryByDynamicFilter(ProductFilters productFilters) {
        Query query = getQueryByGender(productFilters.gender());
        if (productFilters.minPrice() != null) {
            query.addCriteria(Criteria.where("price").gte(productFilters.minPrice()));
        }
        if (productFilters.maxPrice() != null) {
            query.addCriteria(Criteria.where("price").lte(productFilters.maxPrice()));
        }
        if (productFilters.brand() != null) {
            query.addCriteria(Criteria.where("brand").is(productFilters.brand()));
        }
        return query;
    }
}
