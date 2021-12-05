package by.karas.onlinestore.models;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CartRecord {
    private Long user_id;
    private Long product_id;
    @NotNull(message = "The field can not be empty")
    @Min (value = 1, message = "Quantity must be more than 0")
    private Long quantity;

    public CartRecord() {
    }

    public CartRecord(Long user_id, Long product_id, Long quantity) {
        this.user_id = user_id;
        this.product_id = product_id;
        this.quantity = quantity;
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
