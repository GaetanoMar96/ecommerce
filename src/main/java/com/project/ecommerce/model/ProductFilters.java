package com.project.ecommerce.model;


import java.util.Set;

public record ProductFilters(String gender,
                             Double minPrice,
                             Double maxPrice,
                             Set<String> colors,
                             String brand) {

}
