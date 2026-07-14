package com.examly.springapp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class VeganSnack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String snackName;
    private String snackType;
    private String quantity;
    private double price;
    private String expiryInMonths;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getSnackName() { return snackName; }
    public void setSnackName(String snackName) { this.snackName = snackName; }
    public String getSnackType() { return snackType; }
    public void setSnackType(String snackType) { this.snackType = snackType; }
    public String getQuantity() { return quantity; }
    public void setQuantity(String quantity) { this.quantity = quantity; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public String getExpiryInMonths() { return expiryInMonths; }
    public void setExpiryInMonths(String expiryInMonths) { this.expiryInMonths = expiryInMonths; }
}
