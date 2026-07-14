package com.vegansnacks.app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "vendors")
public class Vendor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotBlank(message = "Business name is required")
    @Size(max = 200, message = "Business name must not exceed 200 characters")
    @Column(name = "business_name", nullable = false, length = 200)
    private String businessName;

    @NotBlank(message = "Business license number is required")
    @Column(name = "business_license_number", unique = true, nullable = false, length = 100)
    private String businessLicenseNumber;

    @Size(max = 50, message = "Tax ID must not exceed 50 characters")
    @Column(name = "tax_id", length = 50)
    private String taxId;

    @Size(max = 500, message = "Business address must not exceed 500 characters")
    @Column(name = "business_address", length = 500)
    private String businessAddress;

    @Size(max = 20, message = "Business phone must not exceed 20 characters")
    @Column(name = "business_phone", length = 20)
    private String businessPhone;

    @Email(message = "Business email must be a valid format")
    @Column(name = "business_email", length = 100)
    private String businessEmail;

    @Min(value = 1900, message = "Established year must be 1900 or later")
    @Column(name = "established_year")
    private Integer establishedYear;

    @Size(max = 2000, message = "Business description must not exceed 2000 characters")
    @Column(name = "business_description", length = 2000)
    private String businessDescription;

    @Enumerated(EnumType.STRING)
    @Column(name = "approval_status", length = 20)
    private VendorStatus approvalStatus = VendorStatus.PENDING;

    @Column(name = "approval_date")
    private LocalDateTime approvalDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by")
    private User approvedBy;

    // ---- Getters and Setters ----

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getBusinessLicenseNumber() {
        return businessLicenseNumber;
    }

    public void setBusinessLicenseNumber(String businessLicenseNumber) {
        this.businessLicenseNumber = businessLicenseNumber;
    }

    public String getTaxId() {
        return taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    public String getBusinessAddress() {
        return businessAddress;
    }

    public void setBusinessAddress(String businessAddress) {
        this.businessAddress = businessAddress;
    }

    public String getBusinessPhone() {
        return businessPhone;
    }

    public void setBusinessPhone(String businessPhone) {
        this.businessPhone = businessPhone;
    }

    public String getBusinessEmail() {
        return businessEmail;
    }

    public void setBusinessEmail(String businessEmail) {
        this.businessEmail = businessEmail;
    }

    public Integer getEstablishedYear() {
        return establishedYear;
    }

    public void setEstablishedYear(Integer establishedYear) {
        this.establishedYear = establishedYear;
    }

    public String getBusinessDescription() {
        return businessDescription;
    }

    public void setBusinessDescription(String businessDescription) {
        this.businessDescription = businessDescription;
    }

    public VendorStatus getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(VendorStatus approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public LocalDateTime getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(LocalDateTime approvalDate) {
        this.approvalDate = approvalDate;
    }

    public User getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(User approvedBy) {
        this.approvedBy = approvedBy;
    }
}
