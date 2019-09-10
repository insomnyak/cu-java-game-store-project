package com.company.ReneSerulleU1Capstone.model;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Abstract class for store items.
 */
public abstract class Item {

    @NotNull
    @Digits(integer = 3, fraction = 2, message = "Invalid subtotal. Can contain up to 5 digits, " +
            "2 of which are decimals.")
    @Min(value = 0, message = "Invalid quantity. Must be greater than or equal to 0.")
    private BigDecimal price;

    @NotNull
    @Digits(integer = 11, fraction = 0, message = "Invalid quantity. Must be a whole number up to 11 digits long.")
    @Min(value = 0, message = "Invalid quantity. Must be greater than or equal to 0.")
    private Long quantity;

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

}
