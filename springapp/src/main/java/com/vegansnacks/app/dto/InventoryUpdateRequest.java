package com.vegansnacks.app.dto;

import jakarta.validation.constraints.*;

public class InventoryUpdateRequest {

    @NotNull(message = "Current stock is required")
    @Min(value = 0, message = "Current stock cannot be negative")
    private Integer currentStock;

    @Min(value = 0, message = "Reorder point cannot be negative")
    private Integer reorderPoint;

    @Min(value = 1, message = "Max stock must be at least 1")
    private Integer maxStock;

    @DecimalMin(value = "0.0", message = "Cost per unit cannot be negative")
    @Digits(integer = 10, fraction = 2, message = "Cost per unit can have at most 2 decimal places")
    private Double costPerUnit;

    @Size(max = 200, message = "Location must not exceed 200 characters")
    private String location;

    // ---- Getters and Setters ----

    public Integer getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(Integer currentStock) {
        this.currentStock = currentStock;
    }

    public Integer getReorderPoint() {
        return reorderPoint;
    }

    public void setReorderPoint(Integer reorderPoint) {
        this.reorderPoint = reorderPoint;
    }

    public Integer getMaxStock() {
        return maxStock;
    }

    public void setMaxStock(Integer maxStock) {
        this.maxStock = maxStock;
    }

    public Double getCostPerUnit() {
        return costPerUnit;
    }

    public void setCostPerUnit(Double costPerUnit) {
        this.costPerUnit = costPerUnit;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
