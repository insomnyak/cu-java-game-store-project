package com.company.ReneSerulleU1Capstone.model;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Objects;

public class TShirt {

    @Digits(integer = 11, fraction = 0, message = "Invalid quantity. Must be a whole number up to 11 digits long.")
    private Long tShirtId;

    @NotBlank(message = "Invalid size: cannot be empty or blank.")
    @Size(max = 20, message = "Invalid size: must not be longer than 20 characters.")
    private String size;

    @NotBlank(message = "Invalid color: cannot be empty or blank.")
    @Size(max = 20, message = "Invalid color: must not be longer than 20 characters.")
    private String color;

    @NotBlank(message = "Invalid description: cannot be empty or blank.")
    @Size(max = 255, message = "Invalid description: must not be longer than 255 characters.")
    private String description;

    @NotNull
    @Digits(integer = 5, fraction = 2, message = "Invalid price. Can contain up to 5 whole number digits " +
            "and 2 decimals.")
    private BigDecimal price;

    @NotNull
    @Digits(integer = 11, fraction = 0, message = "Invalid quantity. Must be a whole number up to 11 digits long.")
    private Long quantity;

    public Long getTShirtId() {
        return tShirtId;
    }

    public void setTShirtId(Long tShirtId) {
        this.tShirtId = tShirtId;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TShirt)) return false;
        TShirt tShirt = (TShirt) o;
        return Objects.equals(getTShirtId(), tShirt.getTShirtId()) &&
                getSize().equals(tShirt.getSize()) &&
                getColor().equals(tShirt.getColor()) &&
                getDescription().equals(tShirt.getDescription()) &&
                getPrice().equals(tShirt.getPrice()) &&
                getQuantity().equals(tShirt.getQuantity());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTShirtId(), getSize(), getColor(), getDescription(), getPrice(), getQuantity());
    }
}
