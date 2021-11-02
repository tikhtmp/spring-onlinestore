package by.karas.onlinestore.dao;

import by.karas.onlinestore.models.Product;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProductDAO {
    private List<Product> products;

    {
        products = new ArrayList<>();
        products.add(new Product(1,
                        "Product1",
                "short description for Product1",
                "detail description for Product1",
                 new BigDecimal(1.11)));
        products.add(new Product(2,
                        "Product2",
                "short description for Product2",
                "detail description for Product2",
                new BigDecimal(2.22)));
        products.add(new Product(3,
                        "Product3",
                "short description for Product3",
                "detail description for Product3",
                new BigDecimal(1.11)));
    }

    public List<Product> productIndex(){
        return products;
    }

    public Product getProductById(int id){
        return products.stream().filter(product -> product.getId() == id).findAny().orElse(null);
    }

}
