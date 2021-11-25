package by.karas.onlinestore.dao;

import by.karas.onlinestore.models.CartRecord;
import by.karas.onlinestore.models.Product;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CartDAO {
    private final JdbcTemplate jdbcTemplate;
    private final ProductDAO productDAO;

    public CartDAO(JdbcTemplate jdbcTemplate, ProductDAO productDAO) {
        this.jdbcTemplate = jdbcTemplate;
        this.productDAO = productDAO;
    }

    public Map<Product, Long> getCartByUserLogin(String login){
        Map<Product, Long> cart = new HashMap<>();
        final String sql = "select * from cart where user=?";
        List <CartRecord> cartRecords = jdbcTemplate.query(
                sql, new PreparedStatementSetter() {
                    public void setValues(PreparedStatement preparedStatement) throws SQLException {
                        preparedStatement.setString(1, login);
                    }
                },
                new BeanPropertyRowMapper<>(CartRecord.class));

        for(CartRecord cartRecord : cartRecords){
            cart.put(productDAO.getProduct(cartRecord.getProduct()), cartRecord.getQuantity());
        }

        return cart;
    }

    public void addProductToCart(String login, Long id, Long quantity) {
        CartRecord cartRecord = new CartRecord(login, id, quantity);

        if(isRecordExist(login, id)) {
            update(cartRecord, quantity);
        } else {
            save(cartRecord);
        }
    }

    public void update(CartRecord cartRecordToUpdate, Long quantity){
        final String sql = "update cart set quantity=? where user=? and product=?";
            jdbcTemplate.update(sql
                    , cartRecordToUpdate.getUser()
                    , cartRecordToUpdate.getProduct()
                    , quantity);
    }

    public void save(CartRecord newCartRecord){
            final String sql = "insert into (user, product, quantity) values(?, ? ?)";
            jdbcTemplate.update(sql
                    , newCartRecord.getUser()
                    , newCartRecord.getProduct()
                    , newCartRecord.getQuantity());
    }

    private boolean isRecordExist(String login, Long id) {
        final String sql = "select * from cart where login=? and id=?";

        List<CartRecord> cartRecords = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(CartRecord.class));

//        if (cartRecords.size() > 1){
//            try {
//                throw new Exception("Duplicate cart records for user " + login);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }

        return  cartRecords.size() == 0 ? false : true;
    }


//    ------------------------------------------------------
//        public List<CartRecord> getCartProducts(String userLogin){
//        List<CartRecord> records = new ArrayList<>();
//        final String sql = "select * from cart where user=?";
//        records =jdbcTemplate.query(
//                sql, new PreparedStatementSetter() {
//                    public void setValues(PreparedStatement preparedStatement) throws SQLException {
//                        preparedStatement.setString(1, userLogin);
//                    }
//                },
//                new BeanPropertyRowMapper<>(CartRecord.class));
//
//        System.out.println("Cart tmp:");
//
//        for(CartRecord record : records){
//            System.out.println(record.getUser() + " : " + record.getProduct());
//        }
//
//        return records;
//    }
//    public List<CartRecord> getAllCartProducts(){
//        List<CartRecord> records = new ArrayList<>();
//        final String sql = "select * from cart";
//
//        records =jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(CartRecord.class));
//        return records;
//    }

}
