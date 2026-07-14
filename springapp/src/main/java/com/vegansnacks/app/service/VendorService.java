package com.vegansnacks.app.service;

import com.vegansnacks.app.dto.VendorOnboardRequest;
import com.vegansnacks.app.dto.VendorResponse;
import com.vegansnacks.app.entity.User;
import com.vegansnacks.app.entity.Vendor;
import com.vegansnacks.app.entity.VendorStatus;
import com.vegansnacks.app.repository.UserRepository;
import com.vegansnacks.app.repository.VendorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Vendor service handling onboarding requests, verification, and approval workflows.
 */
@Service
public class VendorService {

    private final VendorRepository vendorRepository;
    private final UserRepository userRepository;

    public VendorService(VendorRepository vendorRepository, UserRepository userRepository) {
        this.vendorRepository = vendorRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public VendorResponse onboardVendor(VendorOnboardRequest request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + username));

        if (vendorRepository.findByUserId(user.getId()).isPresent()) {
            throw new IllegalArgumentException("Vendor profile already exists for user: " + username);
        }

        if (vendorRepository.existsByBusinessLicenseNumber(request.getBusinessLicenseNumber())) {
            throw new IllegalArgumentException("Business license number '" +
                    request.getBusinessLicenseNumber() + "' is already registered.");
        }

        Vendor vendor = new Vendor();
        vendor.setUser(user);
        vendor.setBusinessName(request.getBusinessName());
        vendor.setBusinessLicenseNumber(request.getBusinessLicenseNumber());
        vendor.setTaxId(request.getTaxId());
        vendor.setBusinessAddress(request.getBusinessAddress());
        vendor.setBusinessPhone(request.getBusinessPhone());
        vendor.setBusinessEmail(request.getBusinessEmail());
        vendor.setEstablishedYear(request.getEstablishedYear());
        vendor.setBusinessDescription(request.getBusinessDescription());
        vendor.setApprovalStatus(VendorStatus.PENDING);

        Vendor saved = vendorRepository.save(vendor);
        return mapToResponse(saved);
    }

    public List<VendorResponse> getAllVendors() {
        return vendorRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public VendorResponse getVendorById(Long id) {
        Vendor vendor = vendorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vendor not found with id: " + id));
        return mapToResponse(vendor);
    }

    public VendorResponse getVendorByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + username));
        Vendor vendor = vendorRepository.findByUserId(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Vendor profile not found for user: " + username));
        return mapToResponse(vendor);
    }

    public List<VendorResponse> getVendorsByStatus(VendorStatus status) {
        return vendorRepository.findByApprovalStatus(status)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public VendorResponse updateApprovalStatus(Long vendorId, String status, String approverUsername) {
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new EntityNotFoundException("Vendor not found with id: " + vendorId));

        User approver = userRepository.findByUsername(approverUsername)
                .orElseThrow(() -> new EntityNotFoundException("Approver not found: " + approverUsername));

        VendorStatus vendorStatus;
        try {
            vendorStatus = VendorStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid vendor status: " + status +
                    ". Valid statuses are: PENDING, APPROVED, REJECTED, SUSPENDED");
        }

        vendor.setApprovalStatus(vendorStatus);
        vendor.setApprovalDate(LocalDateTime.now());
        vendor.setApprovedBy(approver);

        Vendor updated = vendorRepository.save(vendor);
        return mapToResponse(updated);
    }

    private VendorResponse mapToResponse(Vendor entity) {
        VendorResponse response = new VendorResponse();
        response.setId(entity.getId());
        response.setUserId(entity.getUser() != null ? entity.getUser().getId() : null);
        response.setUsername(entity.getUser() != null ? entity.getUser().getUsername() : null);
        response.setBusinessName(entity.getBusinessName());
        response.setBusinessLicenseNumber(entity.getBusinessLicenseNumber());
        response.setTaxId(entity.getTaxId());
        response.setBusinessAddress(entity.getBusinessAddress());
        response.setBusinessPhone(entity.getBusinessPhone());
        response.setBusinessEmail(entity.getBusinessEmail());
        response.setEstablishedYear(entity.getEstablishedYear());
        response.setBusinessDescription(entity.getBusinessDescription());
        response.setApprovalStatus(entity.getApprovalStatus() != null ? entity.getApprovalStatus().name() : null);
        response.setApprovalDate(entity.getApprovalDate());
        response.setApprovedById(entity.getApprovedBy() != null ? entity.getApprovedBy().getId() : null);
        return response;
    }
}
