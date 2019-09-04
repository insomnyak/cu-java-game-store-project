package com.company.ReneSerulleU1Capstone.model;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Objects;

public class Console extends Item {

    @Digits(integer = 11, fraction = 0, message = "Invalid consoleId. Must be a whole number up to 11 digits long.")
    private Long consoleId;

    @NotBlank(message = "Invalid model: cannot be empty or blank.")
    @Size(max = 50, message = "Invalid model: must not be longer than 50 characters.")
    private String model;

    @NotBlank(message = "Invalid manufacturer: cannot be empty or blank.")
    @Size(max = 50, message = "Invalid manufacturer: must not be longer than 50 characters.")
    private String manufacturer;

    @Size(max = 20, message = "Invalid memoryCount: must not be longer than 20 characters.")
    private String memoryAmount;

    @Size(max = 20, message = "Invalid processor: must not be longer than 20 characters.")
    private String processor;

    public Long getConsoleId() {
        return consoleId;
    }

    public void setConsoleId(Long consoleId) {
        this.consoleId = consoleId;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getMemoryAmount() {
        return memoryAmount;
    }

    public void setMemoryAmount(String memoryAmount) {
        this.memoryAmount = memoryAmount;
    }

    public String getProcessor() {
        return processor;
    }

    public void setProcessor(String processor) {
        this.processor = processor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Console)) return false;
        Console console = (Console) o;
        return Objects.equals(getConsoleId(), console.getConsoleId()) &&
                getModel().equals(console.getModel()) &&
                getManufacturer().equals(console.getManufacturer()) &&
                Objects.equals(getMemoryAmount(), console.getMemoryAmount()) &&
                Objects.equals(getProcessor(), console.getProcessor()) &&
                getPrice().equals(console.getPrice()) &&
                getQuantity().equals(console.getQuantity());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getConsoleId(), getModel(), getManufacturer(), getMemoryAmount(), getProcessor(), getPrice(),
                getQuantity());
    }
}
