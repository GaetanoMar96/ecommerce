package com.project.ecommerce.model;


public record ProductFilters(String gender,
                             Double minPrice,
                             Double maxPrice,
                             String color,
                             String brand) {

}
