package com.vegansnacks.app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "vegan_snacks")
@EntityListeners(AuditingEntityListener.class)
public class VeganSnackEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id")
    private User vendor;

    @NotBlank(message = "Snack name is required")
    @Size(min = 2, max = 100, message = "Snack name must be between 2 and 100 characters")
    @Column(name = "snack_name", nullable = false, length = 100)
    private String snackName;

    @Size(max = 50, message = "Snack type must not exceed 50 characters")
    @Column(name = "snack_type", length = 50)
    private String snackType;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    @Column(length = 1000)
    private String description;

    @Column(length = 2000)
    private String ingredients;

    @Column(name = "nutritional_info", columnDefinition = "TEXT")
    private String nutritionalInfo;

    @NotBlank(message = "Quantity is required")
    @Size(min = 1, max = 20, message = "Quantity must be between 1 and 20 characters")
    @Column(nullable = false, length = 20)
    private String quantity;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    @Column(nullable = false)
    private Double price;

    @Min(value = 0, message = "Expiry in months must be at least 0")
    @Max(value = 60, message = "Expiry in months must not exceed 60")
    @Column(name = "expiry_in_months")
    private Integer expiryInMonths;

    @Column(unique = true, length = 50)
    private String sku;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private SnackStatus status = SnackStatus.DRAFT;

    @CreatedDate
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "last_modified")
    private LocalDateTime lastModified;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by")
    private User approvedBy;

    @Column(name = "approval_date")
    private LocalDateTime approvalDate;

    // ---- Getters and Setters ----

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getVendor() {
        return vendor;
    }

    public void setVendor(User vendor) {
        this.vendor = vendor;
    }

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

    public SnackStatus getStatus() {
        return status;
    }

    public void setStatus(SnackStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    public void setLastModified(LocalDateTime lastModified) {
        this.lastModified = lastModified;
    }

    public User getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(User approvedBy) {
        this.approvedBy = approvedBy;
    }

    public LocalDateTime getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(LocalDateTime approvalDate) {
        this.approvalDate = approvalDate;
    }
}
