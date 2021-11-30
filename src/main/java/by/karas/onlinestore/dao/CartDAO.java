package by.karas.onlinestore.dao;

import by.karas.onlinestore.models.CartRecord;
import by.karas.onlinestore.models.Product;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CartDAO {
    private final JdbcTemplate jdbcTemplate;
    private final ProductDAO productDAO;
    private final UserDAO userDAO;

    public CartDAO(JdbcTemplate jdbcTemplate, ProductDAO productDAO, UserDAO userDAO) {
        this.jdbcTemplate = jdbcTemplate;
        this.productDAO = productDAO;
        this.userDAO = userDAO;
    }

    public Long getQuantityFromCart(Long userId, Long productId) {
//        Long userId = userDAO.getUserByLogin(login).getId();
        final String sql = "select * from cart where user_id=? and product_id=?";

        List<CartRecord> cartRecords = jdbcTemplate.query(
                sql, new PreparedStatementSetter() {
                    public void setValues(PreparedStatement preparedStatement) throws SQLException {
                        preparedStatement.setLong(1, userId);
                        preparedStatement.setLong(2, productId);
                    }
                },
                new BeanPropertyRowMapper<>(CartRecord.class));

        if (cartRecords.size() == 0) {
            return 0L;
        }

        return cartRecords.get(0).getQuantity();
    }

    public Map<Product, Long> getCartByUserId(Long id) {
//        Long userId = userDAO.getUserByLogin(login).getId();
        Map<Product, Long> cart = new HashMap<>();
        final String sql = "select * from cart where user_id=?";

        List<CartRecord> cartRecords = jdbcTemplate.query(
                sql, new PreparedStatementSetter() {
                    public void setValues(PreparedStatement preparedStatement) throws SQLException {
                        preparedStatement.setLong(1, id);
                    }
                },
                new BeanPropertyRowMapper<>(CartRecord.class));

        for (CartRecord cartRecord : cartRecords) {
            System.out.println("cart rec = " + cartRecord.getProduct_id());
        }

        for (CartRecord cartRecord : cartRecords) {
            cart.put(productDAO.getProduct(cartRecord.getProduct_id()), cartRecord.getQuantity());
        }

        return cart;
    }

    public Map<Product, Long> getCartByUserLogin(String login) {
        Long userId = userDAO.getUserByLogin(login).getId();
        Map<Product, Long> cart = new HashMap<>();
        final String sql = "select * from cart where user_id=?";
        List<CartRecord> cartRecords = jdbcTemplate.query(
                sql, new PreparedStatementSetter() {
                    public void setValues(PreparedStatement preparedStatement) throws SQLException {
                        preparedStatement.setLong(1, userId);
                    }
                },
                new BeanPropertyRowMapper<>(CartRecord.class));

        for (CartRecord cartRecord : cartRecords) {
            cart.put(productDAO.getProduct(cartRecord.getProduct_id()), cartRecord.getQuantity());
        }

        return cart;
    }

    public CartRecord getCartRecord(String login, Long id) {
        final String sql = "select * from cart where user=? and product=?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(CartRecord.class)).get(0);
    }

//    public void addProductToCart(String login, Long id, Long quantity) {
//        CartRecord cartRecord = new CartRecord(login, id, quantity);
//
//        if(isRecordExist(login, id)) {
//            update(cartRecord, quantity);
//        } else {
//            save(cartRecord);
//        }
//    }

    public void update(CartRecord cartRecordToUpdate, Long quantity) {
        final String sql = "update cart set quantity=? where user=? and product=?";
        jdbcTemplate.update(sql
                , quantity
                , cartRecordToUpdate.getUser_id()
                , cartRecordToUpdate.getProduct_id()
                );
    }

    public void save(CartRecord newCartRecord) {
        if (!isRecordExist(newCartRecord.getUser_id(), newCartRecord.getProduct_id())) {
            final String sql = "insert into cart (user_id, product_id, quantity) values(?, ?, ?)";
            jdbcTemplate.update(sql
                    , newCartRecord.getUser_id()
                    , newCartRecord.getProduct_id()
                    , newCartRecord.getQuantity());
        }
    }

    private boolean isRecordExist(Long userId, Long productId) {
        final String sql = "select * from cart where user_id=" + userId + " and product_id=" + productId;

        List<CartRecord> cartRecords = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(CartRecord.class));

//        if (cartRecords.size() > 1){
//            try {
//                throw new Exception("Duplicate cart records for user " + login);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }

        return cartRecords.size() == 0 ? false : true;
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
