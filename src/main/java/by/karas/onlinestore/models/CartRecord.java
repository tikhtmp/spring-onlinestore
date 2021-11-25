package by.karas.onlinestore.models;

public class CartRecord {
    private String user;
    private Long product;
    private Long quantity;

    public CartRecord() {
    }

    public CartRecord(String user, Long product, Long quantity) {
        this.user = user;
        this.product = product;
        this.quantity = quantity;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Long getProduct() {
        return product;
    }

    public void setProduct(Long product) {
        this.product = product;
    }
}
