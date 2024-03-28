package com.example.oauth.controller;

import com.example.oauth.dao.models.Product;
import com.example.oauth.dto.ProductDto;
import com.example.oauth.service.ProductService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping("/products")
    List<ProductDto> getProducts() {
        List<Product> products = service.findProducts();
        return products.stream()
                .map(product -> new ProductDto(
                        product.getId(),
                        product.getTitle(),
                        product.getDescription(),
                        product.getImage())
                ).toList();
    }

    @GetMapping("/products/{id}")
    ProductDto getProduct(@PathVariable("id") int id) {
        Product product = service.findProductById(id);
        return new ProductDto(
                product.getId(),
                product.getTitle(),
                product.getDescription(),
                product.getImage()
        );
    }

    @PostMapping("/products")
    public void addProduct(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody ProductDto productDto) {
        Product product = new Product();
        product.setTitle(productDto.title());
        product.setDescription(productDto.description());
        product.setImage(productDto.image());
        service.addProduct(product);
    }
}
