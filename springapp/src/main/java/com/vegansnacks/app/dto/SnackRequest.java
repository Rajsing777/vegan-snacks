package com.vegansnacks.app.dto;

import jakarta.validation.constraints.*;

public class SnackRequest {

    @NotBlank(message = "Snack name is required")
    @Size(min = 2, max = 100, message = "Snack name must be between 2 and 100 characters")
    @Pattern(regexp = "^[a-zA-Z0-9 ]+$", message = "Snack name must be alphanumeric with spaces only")
    private String snackName;

    @Size(max = 50, message = "Snack type must not exceed 50 characters")
    private String snackType;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    @NotBlank(message = "Ingredients are required")
    @Pattern(regexp = "^[a-zA-Z0-9 ,]+$", message = "Ingredients must be a comma-separated list of alphanumeric values")
    private String ingredients;

    private String nutritionalInfo;

    @NotBlank(message = "Quantity is required")
    @Size(min = 1, max = 20, message = "Quantity must be between 1 and 20 characters")
    private String quantity;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    @Digits(integer = 10, fraction = 2, message = "Price can have at most 2 decimal places")
    private Double price;

    @NotNull(message = "Expiry in months is required")
    @Min(value = 0, message = "Expiry in months must be at least 0")
    @Max(value = 60, message = "Expiry in months must not exceed 60")
    private Integer expiryInMonths;

    @Size(max = 50, message = "SKU must not exceed 50 characters")
    private String sku;

    // ---- Getters and Setters ----

    public String getSnackName() {
        return snackName;
    }

    public void setSnackName(String snackName) {
        this.snackName = snackName;
    }

    public String getSnackType() {
        return snackType;
    }

    public void setSnackType(String snackType) {
        this.snackType = snackType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getNutritionalInfo() {
        return nutritionalInfo;
    }

    public void setNutritionalInfo(String nutritionalInfo) {
        this.nutritionalInfo = nutritionalInfo;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getExpiryInMonths() {
        return expiryInMonths;
    }

    public void setExpiryInMonths(Integer expiryInMonths) {
        this.expiryInMonths = expiryInMonths;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }
}
