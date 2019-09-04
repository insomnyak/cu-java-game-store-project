package com.company.ReneSerulleU1Capstone.model;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public abstract class Item {

    @NotNull
    @Digits(integer = 5, fraction = 2, message = "Invalid price. Can contain up to 5 whole number digits " +
            "and 2 decimals.")
    private BigDecimal price;

    @Digits(integer = 11, fraction = 0, message = "Invalid quantity. Must be a whole number up to 11 digits long.")
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
