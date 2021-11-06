package by.karas.onlinestore.dao;

import by.karas.onlinestore.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
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
        return jdbcTemplate.query("select * from products", new ProductMapper());
    }

    public Product show(Long id) {
        return jdbcTemplate.query("select * from products where id=?", new Object[]{id}, new ProductMapper())
                .stream().findAny().orElse(null);
    }

    public void save(Product product) {

//        try {
//            PreparedStatement preparedStatement = connection.prepareStatement(
//                    "insert into products values(?, ?, ?, ?, ?)");
//
//            preparedStatement.setLong(1, product.getId());
//            preparedStatement.setString(2, product.getName());
//            preparedStatement.setString(3, product.getShortDescription());
//            preparedStatement.setString(4, product.getDetailDescription());
//            preparedStatement.setBigDecimal(5, product.getPrice());
//
//            preparedStatement.executeUpdate();
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

//    public void update(Long id, Product updatedProduct) {
//        try {
//            PreparedStatement preparedStatement =
//                    connection.prepareStatement("update products set name=?, short_description=?, detail_description=?, price=? where id=?");
//
//            preparedStatement.setString(1, updatedProduct.getName());
//            preparedStatement.setString(2, updatedProduct.getShortDescription());
//            preparedStatement.setString(3, updatedProduct.getDetailDescription());
//            preparedStatement.setBigDecimal(4, updatedProduct.getPrice());
//            preparedStatement.setLong(5, id);
//            preparedStatement.executeUpdate();
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void delete(Long id) {
//        try {
//            PreparedStatement preparedStatement =
//                    connection.prepareStatement("delete from products where id=?");
//            preparedStatement.setLong(1, id);
//            preparedStatement.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
    }
}
