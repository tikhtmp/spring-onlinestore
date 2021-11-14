package by.karas.onlinestore.dao;

import by.karas.onlinestore.models.User;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserDAO {
    private final JdbcTemplate jdbcTemplate;

    public UserDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<User> getAllUsers(){
        return jdbcTemplate.query("select * from users", new BeanPropertyRowMapper<>(User.class));
    }

    public User getUser(String login) {
        final String sql = "select * from users where login=?";
        List <User> products = jdbcTemplate.query(
                sql, new PreparedStatementSetter() {

                    public void setValues(PreparedStatement preparedStatement) throws SQLException {
                        preparedStatement.setString(1, login);
                    }
                },
                new BeanPropertyRowMapper<>(User.class));
        return products.get(0);
    }

    public List<User> getUsers(String request) {
        List<User> users = new ArrayList<>();
        for(User item : getAllUsers()){
            if(item.getName().contains(request)){
                users.add(item);
            }
        }
        return users;
    }

    public void save (User user){
        String sql = "insert into users (name, login, password, role) values(?, ?, ?, ?)";
        jdbcTemplate.update(sql
                , user.getName()
                , user.getLogin()
                , user.getPassword()
                , null);
    }

    public void update(String login, User updatedUser){
        String sql = "update users set name=?, login=?, password=? where login=?";
        jdbcTemplate.update(sql
                , updatedUser.getName()
                , updatedUser.getLogin()
                , updatedUser.getPassword()
                , login);
    }

    public void delete(String login){
        String sql = "delete from users where login=?";
        jdbcTemplate.update(sql, login);
    }

}
