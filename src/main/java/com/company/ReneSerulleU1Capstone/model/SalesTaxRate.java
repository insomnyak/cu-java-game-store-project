package com.company.ReneSerulleU1Capstone.model;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Objects;

public class SalesTaxRate {

    @NotBlank(message = "Invalid size: cannot be empty or blank.")
    @Size(max = 2, min = 2, message = "Invalid size: must be exactly 2 characters long.")
    private String state;

    @NotNull
    @Digits(integer = 1, fraction = 2, message = "Invalid subtotal. Can contain up to 3 digits, " +
            "2 of which are decimals.")
    private BigDecimal rate;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SalesTaxRate)) return false;
        SalesTaxRate that = (SalesTaxRate) o;
        return getState().equals(that.getState()) &&
                getRate().equals(that.getRate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getState(), getRate());
    }
}
