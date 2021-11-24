package by.karas.onlinestore.models;

public class CartRecord {
    private String user;
    private Long product;

    public CartRecord() {
    }

    public CartRecord(String user, Long product) {
        this.user = user;
        this.product = product;
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
