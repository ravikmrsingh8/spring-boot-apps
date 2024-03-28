package com.example.feign.config.feign;


import com.example.feign.dto.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

/**
 * Spring Documentation on OpenFeign
 * https://docs.spring.io/spring-cloud-openfeign/docs/current/reference/html/#spring-cloud-feign
 */
@FeignClient(value = "ProductApiClient",
        url="http://localhost:9001",
        configuration = ProductApiClientConfig.class)
public interface ProductApiClient {

    @GetMapping("/products")
    List<Product> getProducts(@RequestHeader("Authorization") String accessToken);

    @GetMapping("/products/{id}")
    Product getProductById(@RequestHeader("Authorization") String accessToken,
                              @PathVariable("id") int id);

    @PostMapping(value = "/products", consumes = "application/json")
    void addProduct(@RequestHeader("Authorization") String accessToken,
                    Product product);
}
