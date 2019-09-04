package com.company.ReneSerulleU1Capstone.model;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Objects;

public class Invoice {

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

    @NotBlank(message = "Invalid itemType: cannot be empty or blank.")
    @Size(max = 20, message = "Invalid itemType: must not be longer than 20 characters.")
    private String itemType;

    @NotNull
    @Digits(integer = 11, fraction = 0, message = "Invalid itemId. Must be a whole number up to 11 digits long.")
    private Long itemId;

    @NotNull
    @Digits(integer = 5, fraction = 2, message = "Invalid price. Can contain up to 5 whole number digits " +
            "and 2 decimals.")
    private BigDecimal unitPrice;

    @NotNull
    @Digits(integer = 11, fraction = 0, message = "Invalid quantity. Must be a whole number up to 11 digits long.")
    private Long quantity;

    @NotNull
    @Digits(integer = 5, fraction = 2, message = "Invalid subtotal. Can contain up to 5 whole number digits " +
            "and 2 decimals.")
    private BigDecimal subtotal;

    @NotNull
    @Digits(integer = 5, fraction = 2, message = "Invalid tax. Can contain up to 5 whole number digits " +
            "and 2 decimals.")
    private BigDecimal tax;

    @NotNull
    @Digits(integer = 5, fraction = 2, message = "Invalid processingFee. Can contain up to 5 whole number digits " +
            "and 2 decimals.")
    private BigDecimal processingFee;

    @NotNull
    @Digits(integer = 5, fraction = 2, message = "Invalid total. Can contain up to 5 whole number digits " +
            "and 2 decimals.")
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
        if (!(o instanceof Invoice)) return false;
        Invoice invoice = (Invoice) o;
        return Objects.equals(getInvoiceId(), invoice.getInvoiceId()) &&
                getName().equals(invoice.getName()) &&
                getStreet().equals(invoice.getStreet()) &&
                getCity().equals(invoice.getCity()) &&
                getState().equals(invoice.getState()) &&
                getZipcode().equals(invoice.getZipcode()) &&
                getItemType().equals(invoice.getItemType()) &&
                getItemId().equals(invoice.getItemId()) &&
                getUnitPrice().equals(invoice.getUnitPrice()) &&
                getQuantity().equals(invoice.getQuantity()) &&
                getSubtotal().equals(invoice.getSubtotal()) &&
                getTax().equals(invoice.getTax()) &&
                getProcessingFee().equals(invoice.getProcessingFee()) &&
                getTotal().equals(invoice.getTotal());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getInvoiceId(), getName(), getStreet(), getCity(), getState(), getZipcode(), getItemType(),
                getItemId(), getUnitPrice(), getQuantity(), getSubtotal(), getTax(), getProcessingFee(), getTotal());
    }
}
