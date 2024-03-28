package com.example.feign.controller;


import com.example.feign.config.feign.ProductApiClient;
import com.example.feign.dto.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductController {

    @Autowired
    ProductApiClient apiClient;

    @GetMapping("/products")
    List<Product> getProducts(@RequestHeader("Authorization") String accessToken) {
        return apiClient.getProducts(accessToken);
    }

    @GetMapping("/products/{id}")
    Product getProductById(@RequestHeader("Authorization") String accessToken,
                           @PathVariable("id") int id) {
        return apiClient.getProductById(accessToken, id);
    }

    @PostMapping("/products")
    public void addProduct(@RequestHeader("Authorization") String accessToken,
                           @RequestBody Product product) {
        apiClient.addProduct(accessToken, product);
    }
}
