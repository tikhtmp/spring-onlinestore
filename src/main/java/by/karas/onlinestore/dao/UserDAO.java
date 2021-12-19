package by.karas.onlinestore.dao;

import by.karas.onlinestore.models.Product;
import by.karas.onlinestore.models.Role;
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

    public User getUserByLogin(String login) {
        final String sql = "select * from users where login=?";
        List <User> users = jdbcTemplate.query(
                sql, new PreparedStatementSetter() {

                    public void setValues(PreparedStatement preparedStatement) throws SQLException {
                        preparedStatement.setString(1, login);
                    }
                },
                new BeanPropertyRowMapper<>(User.class));

        return users.get(0);

    }

    public User getUserById(Long id) {
        final String sql = "select * from users where id=?";
        List <User> users = jdbcTemplate.query(
                sql, new PreparedStatementSetter() {

                    public void setValues(PreparedStatement preparedStatement) throws SQLException {
                        preparedStatement.setLong(1, id);
                    }
                },
                new BeanPropertyRowMapper<>(User.class));

        return users.get(0);
    }

    public List<User> getUsers(String filter) {
        final String sql = "select * from users where login like \'%" + filter + "%\' order by login";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class));
    }

    public void save (User user, Role role){
        String sql = "insert into users (login, password, role) values(?, ?, ?)";
        jdbcTemplate.update(sql
                , user.getLogin()
                , user.getPassword()
                , role.toString());
    }

    public void update(Long id, User updatedUser){
        String sql = "update users set password=?, role=? where id=?";
        jdbcTemplate.update(sql
                , updatedUser.getPassword()
                , updatedUser.getRole().toString()
                , id);
    }

    public void delete(Long id){
        String sql = "delete from users where id=?";
        jdbcTemplate.update(sql, id);
    }

}
