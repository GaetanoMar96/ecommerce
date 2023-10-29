package com.project.ecommerce.model;


public record ProductFilters(String gender,
                             String category,
                             Double minPrice,
                             Double maxPrice,
                             String color) {

}
