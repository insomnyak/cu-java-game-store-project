package com.company.ReneSerulleU1Capstone.model;

import com.company.ReneSerulleU1Capstone.servicelayer.ItemType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true, value = {"$type"})
@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.PROPERTY, property = "$type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Console.class, name = ItemType.console),
        @JsonSubTypes.Type(value = Game.class, name = ItemType.game),
        @JsonSubTypes.Type(value = TShirt.class, name = ItemType.tShirt)
})
public abstract class Item {

    private String $type;

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

    public String get$type() {
        return $type;
    }

    public void set$type(String $type) {
        this.$type = $type;
    }
}
