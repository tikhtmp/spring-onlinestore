package by.karas.onlinestore.models;
import java.math.BigDecimal;

public class Product {
    private int id;
    private String name;
    private String shortDescription;
    private String detailDescription;
    private BigDecimal price;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDetailDescription() {
        return detailDescription;
    }

    public void setDetailDescription(String detailDescription) {
        this.detailDescription = detailDescription;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Product(int id, String name, String shortDescription, String detailDescription, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.shortDescription = shortDescription;
        this.detailDescription = detailDescription;
        this.price = price;
    }
}
