package by.karas.onlinestore.models;

public class CartRecord {
    private Long id;
    private Long user_id;
    private Long product_id;
    private Long quantity;

    public CartRecord() {
    }

    public CartRecord(/*Long id,*/ Long user_id, Long product_id, Long quantity) {
//        this.id = id;
        this.user_id = user_id;
        this.product_id = product_id;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public Long getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Long product_id) {
        this.product_id = product_id;
    }
}
