package com.project.ecommerce.controller;

import com.project.ecommerce.entity.Product;
import com.project.ecommerce.model.ProductFilters;
import com.project.ecommerce.service.ProductsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/v1/ecommerce/products")
@RequiredArgsConstructor
public class ProductsController {

    private final ProductsService productsService;

    @GetMapping("/{gender}")
    public ResponseEntity<Flux<Product>> getProductsByGender(@PathVariable String gender) {
        return ResponseEntity.ok(productsService.findProductsByGender(gender));
    }

    @GetMapping("/search/{query}")
    public ResponseEntity<Flux<Product>> getProductsBySearchQuery(@PathVariable String query) {
        return ResponseEntity.ok(productsService.findProductsBySearchQuery(query));
    }

    @PostMapping("/filters")
    public ResponseEntity<Flux<Product>> getProductsByFilters(@RequestBody ProductFilters productFilters) {
        return ResponseEntity.ok(productsService.findProductsByFilters(productFilters));
    }
}
