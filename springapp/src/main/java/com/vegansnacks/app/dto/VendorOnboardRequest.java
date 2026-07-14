package com.vegansnacks.app.dto;

import jakarta.validation.constraints.*;

public class VendorOnboardRequest {

    @NotBlank(message = "Business name is required")
    @Size(max = 200, message = "Business name must not exceed 200 characters")
    private String businessName;

    @NotBlank(message = "Business license number is required")
    @Size(max = 100, message = "Business license number must not exceed 100 characters")
    private String businessLicenseNumber;

    @Size(max = 50, message = "Tax ID must not exceed 50 characters")
    private String taxId;

    @Size(max = 500, message = "Business address must not exceed 500 characters")
    private String businessAddress;

    @Size(max = 20, message = "Business phone must not exceed 20 characters")
    private String businessPhone;

    @Email(message = "Business email must be a valid format")
    private String businessEmail;

    @Min(value = 1900, message = "Established year must be 1900 or later")
    private Integer establishedYear;

    @Size(max = 2000, message = "Business description must not exceed 2000 characters")
    private String businessDescription;

    // ---- Getters and Setters ----

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
}
