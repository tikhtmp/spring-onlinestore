package by.karas.onlinestore.dao;

import by.karas.onlinestore.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.*;

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
        String sql = "insert into products (name, short_description, detail_description, price) values(?, ?, ?, ?)";
        jdbcTemplate.update(sql, product.getName(), product.getShortDescription(), product.getDetailDescription(), product.getPrice());

//        KeyHolder keyHolder = new GeneratedKeyHolder();
//        jdbcTemplate.update(new PreparedStatementCreator() {
//            @Override
//            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
//                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
//                ps.setString(1, product.getName());
//                ps.setString(2, product.getShortDescription());
//                ps.setString(3, product.getDetailDescription());
//                ps.setBigDecimal(4, product.getPrice());
//                return ps;
//            }
//        }, keyHolder);
//
//        System.out.println(keyHolder.getKeys());
//        System.out.println("keyHolder " + keyHolder.getKey());
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
