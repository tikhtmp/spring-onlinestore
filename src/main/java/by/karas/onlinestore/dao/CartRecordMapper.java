package by.karas.onlinestore.dao;

import by.karas.onlinestore.models.CartRecord;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CartRecordMapper implements RowMapper<CartRecord> {
    @Override
    public CartRecord mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        CartRecord cartRecord = new CartRecord();
//        cartRecord.setId(resultSet.getLong("id"));
        cartRecord.setUser_id(resultSet.getLong("user_id"));
        cartRecord.setProduct_id(resultSet.getLong("product_id"));
        cartRecord.setQuantity(resultSet.getLong("quantity"));
        return null;
    }
}
