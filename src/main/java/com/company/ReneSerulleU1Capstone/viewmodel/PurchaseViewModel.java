package com.company.ReneSerulleU1Capstone.viewmodel;

import com.company.ReneSerulleU1Capstone.model.Item;
import com.company.ReneSerulleU1Capstone.model.ItemType;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Objects;

public class PurchaseViewModel {

    @Digits(integer = 11, fraction = 0, message = "Invalid invoiceId. Must be a whole number up to 11 digits long.")
    private Long invoiceId;

    @NotBlank(message = "Invalid name: cannot be empty or blank.")
    @Size(max = 80, message = "Invalid name: must not be longer than 80 characters.")
    private String name;

    @NotBlank(message = "Invalid street: cannot be empty or blank.")
    @Size(max = 30, message = "Invalid street: must not be longer than 30 characters.")
    private String street;

    @NotBlank(message = "Invalid city: cannot be empty or blank.")
    @Size(max = 30, message = "Invalid city: must not be longer than 30 characters.")
    private String city;

    @NotBlank(message = "Invalid state: cannot be empty or blank.")
    @Size(max = 30, message = "Invalid state: must not be longer than 30 characters.")
    private String state;

    @NotBlank(message = "Invalid zipcode: cannot be empty or blank.")
    @Size(max = 5, message = "Invalid zipcode: must not be longer than 5 characters.")
    private String zipcode;

    private Item item;

    @NotBlank(message = "Invalid itemType: cannot be empty or blank.")
    @Size(max = 20, message = "Invalid itemType: must not be longer than 20 characters.")
    @Pattern(regexp = ItemType.patternCaseSensitive, message = "{invalid.itemType}")
    private String itemType;

    @NotNull
    @Digits(integer = 11, fraction = 0, message = "Invalid itemId. Must be a whole number up to 11 digits long.")
    private Long itemId;

    @Digits(integer = 3, fraction = 2, message = "Invalid subtotal. Can contain up to 5 digits, " +
            "2 of which are decimals.")
    private BigDecimal unitPrice;

    @Digits(integer = 11, fraction = 0, message = "Invalid quantity. Must be a whole number up to 11 digits long.")
    @Min(value = 1, message = "Invalid quantity. Must be at least 1.")
    private Long quantity;

    @Digits(integer = 3, fraction = 2, message = "Invalid subtotal. Can contain up to 5 digits, " +
            "2 of which are decimals.")
    private BigDecimal subtotal;

    @Digits(integer = 3, fraction = 2, message = "Invalid subtotal. Can contain up to 5 digits, " +
            "2 of which are decimals.")
    private BigDecimal tax;

    @Digits(integer = 3, fraction = 2, message = "Invalid subtotal. Can contain up to 5 digits, " +
            "2 of which are decimals.")
    private BigDecimal processingFee;

    @Digits(integer = 3, fraction = 2, message = "Invalid subtotal. Can contain up to 5 digits, " +
            "2 of which are decimals.")
    private BigDecimal total;

    public Long getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public BigDecimal getProcessingFee() {
        return processingFee;
    }

    public void setProcessingFee(BigDecimal processingFee) {
        this.processingFee = processingFee;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PurchaseViewModel)) return false;
        PurchaseViewModel that = (PurchaseViewModel) o;
        return Objects.equals(getInvoiceId(), that.getInvoiceId()) &&
                getName().equals(that.getName()) &&
                getStreet().equals(that.getStreet()) &&
                getCity().equals(that.getCity()) &&
                getState().equals(that.getState()) &&
                getZipcode().equals(that.getZipcode()) &&
                Objects.equals(getItem(), that.getItem()) &&
                getItemType().equals(that.getItemType()) &&
                getItemId().equals(that.getItemId()) &&
                Objects.equals(getUnitPrice(), that.getUnitPrice()) &&
                getQuantity().equals(that.getQuantity()) &&
                Objects.equals(getSubtotal(), that.getSubtotal()) &&
                Objects.equals(getTax(), that.getTax()) &&
                Objects.equals(getProcessingFee(), that.getProcessingFee()) &&
                Objects.equals(getTotal(), that.getTotal());
    }

    @Override
    public int hashCode() {
        return Objects
                .hash(getInvoiceId(), getName(), getStreet(), getCity(), getState(), getZipcode(), getItem(),
                        getItemType(),
                        getItemId(), getUnitPrice(), getQuantity(), getSubtotal(), getTax(), getProcessingFee(),
                        getTotal());
    }
}
