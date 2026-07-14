package com.vegansnacks.app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "product_reviews")
public class ProductReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private VeganSnackEntity product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    @Column(nullable = false)
    private Integer rating;

    @Size(max = 200, message = "Review title must not exceed 200 characters")
    @Column(name = "review_title", length = 200)
    private String reviewTitle;

    @Size(max = 5000, message = "Review text must not exceed 5000 characters")
    @Column(name = "review_text", length = 5000)
    private String reviewText;

    @Column(name = "review_date")
    private LocalDateTime reviewDate;

    @Column(name = "is_verified_purchase")
    private Boolean isVerifiedPurchase = false;

    @Min(value = 0, message = "Helpful votes cannot be negative")
    @Column(name = "helpful_votes")
    private Integer helpfulVotes = 0;

    @Column(name = "is_approved")
    private Boolean isApproved = false;

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

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getReviewTitle() {
        return reviewTitle;
    }

    public void setReviewTitle(String reviewTitle) {
        this.reviewTitle = reviewTitle;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public LocalDateTime getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(LocalDateTime reviewDate) {
        this.reviewDate = reviewDate;
    }

    public Boolean getIsVerifiedPurchase() {
        return isVerifiedPurchase;
    }

    public void setIsVerifiedPurchase(Boolean isVerifiedPurchase) {
        this.isVerifiedPurchase = isVerifiedPurchase;
    }

    public Integer getHelpfulVotes() {
        return helpfulVotes;
    }

    public void setHelpfulVotes(Integer helpfulVotes) {
        this.helpfulVotes = helpfulVotes;
    }

    public Boolean getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(Boolean isApproved) {
        this.isApproved = isApproved;
    }
}
