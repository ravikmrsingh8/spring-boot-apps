package com.example.oauth.service;

import com.example.oauth.controller.ProductNotFoundException;
import com.example.oauth.dao.ProductDao;
import com.example.oauth.dao.models.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductDao dao;

    public ProductService(ProductDao dao) {
        this.dao = dao;
    }

    public List<Product> findProducts() {
        return dao.findProducts();
    }

    public Product findProductById(int id) {
        return dao.findProductById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with id:" + id + " not found"));
    }

    public void addProduct(Product product) {
        dao.addProduct(product);
    }
}
