package by.karas.onlinestore.dao;

import by.karas.onlinestore.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProductDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ProductDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Product> index() {
        return jdbcTemplate.query("select * from products", new BeanPropertyRowMapper<>(Product.class));
    }

    public Product show(Long id) {
        return jdbcTemplate.query("select * from products where id=?", new Object[]{id}, new BeanPropertyRowMapper<>(Product.class))
                .stream().findAny().orElse(null);
    }

    public void save(Product product) {
        jdbcTemplate.update("insert into products values(?, ?, ?, ?, ?)", product.getId(), product.getName(),
                product.getShortDescription(), product.getDetailDescription(), product.getPrice());
    }

    public void update(Long id, Product updatedProduct) {
        jdbcTemplate.update("update products set name=?, short_description=?, detail_description=?, price=? where id=?"
                , updatedProduct.getName()
                , updatedProduct.getShortDescription()
                , updatedProduct.getDetailDescription()
                , updatedProduct.getPrice(),
                id);
    }

    public void delete(Long id) {
        jdbcTemplate.update("delete from products where id=?", id);
    }
}
