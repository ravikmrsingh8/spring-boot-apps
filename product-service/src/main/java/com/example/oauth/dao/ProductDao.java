package com.example.oauth.dao;

import com.example.oauth.dao.models.Product;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ProductDao {

    private final JdbcTemplate jdbcTemplate;

    public ProductDao(JdbcTemplate template) {
        this.jdbcTemplate = template;
    }

    public List<Product> findProducts() {
       return jdbcTemplate.query("SELECT * FROM PRODUCTS", new BeanPropertyRowMapper<>(Product.class));
    }

    public Optional<Product> findProductById(int id) {
        String query = "SELECT * FROM PRODUCTS WHERE id = " + id;
        Product product = jdbcTemplate.queryForObject(query, new BeanPropertyRowMapper<>(Product.class));
        return product == null ? Optional.empty() : Optional.of(product);
    }

    public void addProduct(Product product) {
        String query = "INSERT INTO PRODUCTS(title, description, image) VALUES( ?, ?, ?)";
        jdbcTemplate.update(query, product.getTitle(), product.getDescription(), product.getImage());
    }
}
