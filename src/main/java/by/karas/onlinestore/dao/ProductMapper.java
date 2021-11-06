package by.karas.onlinestore.dao;

import by.karas.onlinestore.models.Product;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


public class ProductMapper implements RowMapper<Product> {

    @Override
    public Product mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Product product = new Product();

        product.setId(resultSet.getLong("id"));
        product.setName(resultSet.getString("name"));
        product.setShortDescription(resultSet.getString("short_description"));
        product.setDetailDescription(resultSet.getString("detail_description"));
        product.setPrice(resultSet.getBigDecimal("price"));
        return null;
    }
}
