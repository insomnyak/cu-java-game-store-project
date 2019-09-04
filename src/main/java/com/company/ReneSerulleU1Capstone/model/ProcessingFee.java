package com.company.ReneSerulleU1Capstone.model;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Objects;

public class ProcessingFee {

    @NotBlank(message = "Invalid productType: cannot be empty or blank.")
    @Size(max = 20, message = "Invalid productType: must not be longer than 20 characters.")
    private String productType;

    @Digits(integer = 4, fraction = 2, message = "Invalid price. Can contain up to 4 whole number digits " +
            "and 2 decimals.")
    private BigDecimal fee;

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProcessingFee)) return false;
        ProcessingFee that = (ProcessingFee) o;
        return getProductType().equals(that.getProductType()) &&
                Objects.equals(getFee(), that.getFee());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getProductType(), getFee());
    }
}
