package by.karas.onlinestore.models;

import com.sun.javafx.beans.IDProperty;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import java.math.BigDecimal;
import java.sql.Date;


public class Product {

    private Long id;

    @NotEmpty(message = "Name must not be empty")
    @Size(min = 3, max = 50, message = "Name must be from 3 to 50 characters")
    private String name;

    @NotEmpty(message = "Description must not be empty")
    @Size(min = 5, max = 50, message = "Description must be from 5 to 50 characters")
    private String shortDescription;

    @NotEmpty(message = "Description must not be empty")
    @Size(min = 5, max = 1000, message = "Description must be from 5 to 1000 characters")
    private String detailDescription;

    @DecimalMin(value = "0.01", message = "Price must greater than 0")
    private BigDecimal price;

    private Date creationDate;
    private Date updateDate;
    private Long author;

    public Product() {
    }

    public Product(Long id, String name, String shortDescription, String detailDescription, BigDecimal price, Date creationDate, Date updateDate, Long author) {
        this.id = id;
        this.name = name;
        this.shortDescription = shortDescription;
        this.detailDescription = detailDescription;
        this.price = price;
        this.creationDate = creationDate;
        this.updateDate = updateDate;
        this.author = author;
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

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Long getAuthor() {
        return author;
    }

    public void setAuthor(Long author) {
        this.author = author;
    }
}
