package by.karas.onlinestore.dao;

import by.karas.onlinestore.models.CartRecord;
import by.karas.onlinestore.models.Product;
import by.karas.onlinestore.models.User;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.*;

@Component
public class ProductDAO {
    private final JdbcTemplate jdbcTemplate;

//    @Autowired
    public ProductDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Product> getAllProducts() {
        return jdbcTemplate.query("select * from products order by name", new BeanPropertyRowMapper<>(Product.class));
    }

    public Product getProduct(Long id) {
        final String sql = "select * from products where id=?";
        List <Product> products = jdbcTemplate.query(
                sql, new PreparedStatementSetter() {

                    public void setValues(PreparedStatement preparedStatement) throws SQLException {
                        preparedStatement.setLong(1, id);
                    }
                },
                new BeanPropertyRowMapper<>(Product.class));
        return products.get(0);
    }
    public List<Product> getCartProducts(Long userId) {

        final String sql = "select * from products where id in (select product_id from cart where user_id=?)";

        List <Product> products = jdbcTemplate.query(
                sql, new PreparedStatementSetter() {
                    public void setValues(PreparedStatement preparedStatement) throws SQLException {
                        preparedStatement.setLong(1, userId);
                    }
                },
                new BeanPropertyRowMapper<>(Product.class));
        return products;
    }

    public List<Product> getProducts(String filter) {
        final String sql = "select * from products where name like \'%" + filter + "%\' order by name";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Product.class));
    }

    public void save(Product product) {

        String sql = "insert into products (name, short_description, detail_description, price, creation_date, update_date, author) values(?, ?, ?, ?, now(), now(), ?)";
        jdbcTemplate.update(sql
                , product.getName()
                , product.getShortDescription()
                , product.getDetailDescription()
                , product.getPrice()
                , product.getAuthor());

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
        jdbcTemplate.update("update products set name=?, short_description=?, detail_description=?, price=?, author=?, update_date=now() where id=?"
                , updatedProduct.getName()
                , updatedProduct.getShortDescription()
                , updatedProduct.getDetailDescription()
                , updatedProduct.getPrice()
                , updatedProduct.getAuthor()
                , id);
    }

    public void delete(Long id) {
        jdbcTemplate.update("delete from products where id=?", id);
    }
}
