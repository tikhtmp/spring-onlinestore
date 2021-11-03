package by.karas.onlinestore.models;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import java.math.BigDecimal;


public class Product {
    private Long id;

    @NotEmpty(message ="Name must not be empty")
    @Size(min = 3, max = 50, message ="Name must be from 3 to 50 characters")
    private String name;

    @NotEmpty(message ="Description must not be empty")
    @Size(min = 5, max = 50, message ="Description must be from 5 to 50 characters")
    private String shortDescription;

    @NotEmpty(message ="Description must not be empty")
    @Size(min = 5, max = 1000, message ="Description must be from 5 to 1000 characters")
    private String detailDescription;

    @DecimalMin(value = "0.01", message ="Price must greater than 0")
    private BigDecimal price;

    public Product() {}

    public Product(Long id, String name, String shortDescription, String detailDescription, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.shortDescription = shortDescription;
        this.detailDescription = detailDescription;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

}
