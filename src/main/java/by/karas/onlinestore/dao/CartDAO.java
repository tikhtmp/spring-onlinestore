package by.karas.onlinestore.dao;

import by.karas.onlinestore.models.CartRecord;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class CartDAO {
    private final JdbcTemplate jdbcTemplate;

    public CartDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }



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
