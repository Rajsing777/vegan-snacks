package com.vegansnacks.app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "inventory")
@EntityListeners(AuditingEntityListener.class)
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private VeganSnackEntity product;

    @NotNull(message = "Current stock is required")
    @Min(value = 0, message = "Current stock cannot be negative")
    @Column(name = "current_stock", nullable = false)
    private Integer currentStock;

    @Min(value = 0, message = "Reorder point cannot be negative")
    @Column(name = "reorder_point")
    private Integer reorderPoint = 10;

    @Min(value = 1, message = "Max stock must be at least 1")
    @Column(name = "max_stock")
    private Integer maxStock = 1000;

    @DecimalMin(value = "0.0", message = "Cost per unit cannot be negative")
    @Column(name = "cost_per_unit")
    private Double costPerUnit;

    @Column(name = "last_restock_date")
    private LocalDateTime lastRestockDate;

    @LastModifiedDate
    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    @Size(max = 200, message = "Location must not exceed 200 characters")
    @Column(length = 200)
    private String location;

    // ---- Getters and Setters ----

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public VeganSnackEntity getProduct() {
        return product;
    }

    public void setProduct(VeganSnackEntity product) {
        this.product = product;
    }

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

    public LocalDateTime getLastRestockDate() {
        return lastRestockDate;
    }

    public void setLastRestockDate(LocalDateTime lastRestockDate) {
        this.lastRestockDate = lastRestockDate;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
