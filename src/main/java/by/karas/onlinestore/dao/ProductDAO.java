package by.karas.onlinestore.dao;

import by.karas.onlinestore.models.Product;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProductDAO {
    private List<Product> products;
    private static Long PEOPLE_COUNT = 3L;

    {
        products = new ArrayList<>();
        products.add(new Product(1L,
                        "Product1",
                "short description for Product1",
                "detail description for Product1",
                 new BigDecimal(1.11)));
        products.add(new Product(2L,
                        "Product2",
                "short description for Product2",
                "detail description for Product2",
                new BigDecimal(2.22)));
        products.add(new Product(3L,
                        "Product3",
                "short description for Product3",
                "detail description for Product3",
                new BigDecimal(1.11)));
    }

    public List<Product> productIndex(){
        return products;
    }

    public Product getProductById(Long id){
        return products.stream().filter(product -> product.getId() == id).findAny().orElse(null);
    }

    public void save(Product product){
        product.setId(++PEOPLE_COUNT);
        products.add(product);
    }

    public void update(Long id, Product product){
        Product productToBeUpdated = getProductById(id);
        productToBeUpdated.setName(product.getName());
        productToBeUpdated.setShortDescription(product.getShortDescription());
        productToBeUpdated.setDetailDescription(product.getDetailDescription());
        productToBeUpdated.setPrice(product.getPrice());
    }

    public void delete(Long id){
        products.removeIf(p -> p.getId() == id);

    }
}
