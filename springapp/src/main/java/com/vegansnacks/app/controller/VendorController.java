package com.vegansnacks.app.controller;

import com.vegansnacks.app.dto.VendorOnboardRequest;
import com.vegansnacks.app.dto.VendorResponse;
import com.vegansnacks.app.entity.VendorStatus;
import com.vegansnacks.app.service.VendorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Vendor controller for business onboarding, verification, and approval management.
 */
@RestController
@RequestMapping("/api/v1/vendors")
@Tag(name = "Vendors", description = "Vendor onboarding, verification, and management workflows")
public class VendorController {

    private final VendorService vendorService;

    public VendorController(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    @PostMapping("/onboard")
    @PreAuthorize("hasRole('VENDOR')")
    @Operation(summary = "Submit vendor onboarding request", description = "Creates a new vendor profile with business license for approval")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Vendor onboarding request submitted"),
            @ApiResponse(responseCode = "400", description = "Invalid input or duplicate license"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - VENDOR role required")
    })
    public ResponseEntity<VendorResponse> onboardVendor(@Valid @RequestBody VendorOnboardRequest request,
                                                         Authentication authentication) {
        VendorResponse response = vendorService.onboardVendor(request, authentication.getName());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('PRODUCT_MANAGER', 'ADMIN')")
    @Operation(summary = "Get all vendors", description = "Returns all registered vendor profiles")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vendors retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<List<VendorResponse>> getAllVendors() {
        return ResponseEntity.ok(vendorService.getAllVendors());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('PRODUCT_MANAGER', 'ADMIN')")
    @Operation(summary = "Get vendor by ID", description = "Returns a specific vendor profile")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vendor retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Vendor not found")
    })
    public ResponseEntity<VendorResponse> getVendorById(@PathVariable Long id) {
        return ResponseEntity.ok(vendorService.getVendorById(id));
    }

    @GetMapping("/my-profile")
    @PreAuthorize("hasRole('VENDOR')")
    @Operation(summary = "Get own vendor profile", description = "Returns the vendor profile of the authenticated user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vendor profile retrieved"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Vendor profile not found")
    })
    public ResponseEntity<VendorResponse> getMyVendorProfile(Authentication authentication) {
        return ResponseEntity.ok(vendorService.getVendorByUsername(authentication.getName()));
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('PRODUCT_MANAGER', 'ADMIN')")
    @Operation(summary = "Get vendors by approval status", description = "Returns vendors filtered by approval status")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vendors retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<List<VendorResponse>> getVendorsByStatus(@PathVariable String status) {
        VendorStatus vendorStatus = VendorStatus.valueOf(status.toUpperCase());
        return ResponseEntity.ok(vendorService.getVendorsByStatus(vendorStatus));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('PRODUCT_MANAGER', 'ADMIN')")
    @Operation(summary = "Update vendor approval status", description = "Approves, rejects, or suspends a vendor")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vendor status updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid status value"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Vendor not found")
    })
    public ResponseEntity<VendorResponse> updateVendorStatus(@PathVariable Long id,
                                                              @RequestParam String status,
                                                              Authentication authentication) {
        VendorResponse response = vendorService.updateApprovalStatus(id, status, authentication.getName());
        return ResponseEntity.ok(response);
    }
}
