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

//    public Product getProductById(Long id){
//        return products.stream().filter(product -> product.getId() == id).findAny().orElse(null);
//    }

    public void save(Product product) {

        //product.setId(++PEOPLE_COUNT);
        try {
            Statement statement = connection.createStatement();
            String SQL = "insert into products values (" + Long.toString(88L) + ",'" + product.getName() + "','" +
                    product.getShortDescription() + "','" + product.getDetailDescription() + "'," + product.getPrice().doubleValue() + ")";
            statement.executeUpdate(SQL);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        products.add(product);
    }

//    public void update(Long id, Product product){
//        Product productToBeUpdated = getProductById(id);
//        productToBeUpdated.setName(product.getName());
//        productToBeUpdated.setShortDescription(product.getShortDescription());
//        productToBeUpdated.setDetailDescription(product.getDetailDescription());
//        productToBeUpdated.setPrice(product.getPrice());
//    }

    public void delete(Long id) {
        products.removeIf(p -> p.getId() == id);

    }
}
