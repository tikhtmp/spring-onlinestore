package by.karas.onlinestore.dao;

import by.karas.onlinestore.models.Product;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProductDAO {
    private List<Product> products;
    private static long PEOPLE_COUNT = 3;

    private static final String URL = "jdbc:mysql://localhost:3306/onlinestore_db";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Rybanka@08";
    private static Connection connection;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public List<Product> index() {
        List<Product> products = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            String SQL = "select * from products";
            ResultSet resultSet = statement.executeQuery(SQL);

            while (resultSet.next()) {
                Product product = new Product();
                product.setId(resultSet.getLong("id"));
                product.setName(resultSet.getString("name"));
                product.setShortDescription(resultSet.getString("short_description"));
                product.setDetailDescription(resultSet.getString("detail_description"));
                product.setPrice(resultSet.getBigDecimal("price"));

                products.add(product);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }

    public Product show(Long id) {
        Product product = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "select * from products where id=?");

            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();

            product = new Product();

            product.setId(resultSet.getLong("id"));
            product.setName(resultSet.getString("name"));
            product.setShortDescription(resultSet.getString("short_description"));
            product.setDetailDescription(resultSet.getString("detail_description"));
            product.setPrice(resultSet.getBigDecimal("price"));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return product;
    }

    public void save(Product product) {

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "insert into products values(?, ?, ?, ?, ?)");

            preparedStatement.setLong(1, product.getId());
            preparedStatement.setString(2, product.getName());
            preparedStatement.setString(3, product.getShortDescription());
            preparedStatement.setString(4, product.getDetailDescription());
            preparedStatement.setBigDecimal(5, product.getPrice());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Long id, Product updatedProduct) {
        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("update products set name=?, short_description=?, detail_description=?, price=? where id=?");

            preparedStatement.setString(1, updatedProduct.getName());
            preparedStatement.setString(2, updatedProduct.getShortDescription());
            preparedStatement.setString(3, updatedProduct.getDetailDescription());
            preparedStatement.setBigDecimal(4, updatedProduct.getPrice());
            preparedStatement.setLong(5, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(Long id) {
        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("delete from products where id=?");
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
}
