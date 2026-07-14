package com.vegansnacks.app.controller;

import com.vegansnacks.app.dto.SnackRequest;
import com.vegansnacks.app.dto.SnackResponse;
import com.vegansnacks.app.service.SnackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Snack controller providing full CRUD endpoints with RBAC scoping.
 * GET endpoints are publicly accessible (GUEST).
 * Create/Update/Delete requires authentication with role-based ownership checks.
 */
@RestController
@RequestMapping("/api/v1/snacks")
@Tag(name = "Vegan Snacks", description = "CRUD operations for vegan snack products")
public class SnackController {

    private final SnackService snackService;

    public SnackController(SnackService snackService) {
        this.snackService = snackService;
    }

    @GetMapping
    @Operation(summary = "Get all snacks", description = "Returns all vegan snack products. Publicly accessible.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Snacks retrieved successfully")
    })
    public ResponseEntity<List<SnackResponse>> getAllSnacks() {
        return ResponseEntity.ok(snackService.getAllSnacks());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get snack by ID", description = "Returns a single vegan snack product by ID. Publicly accessible.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Snack retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Snack not found")
    })
    public ResponseEntity<SnackResponse> getSnackById(@PathVariable Long id) {
        return ResponseEntity.ok(snackService.getSnackById(id));
    }

    @GetMapping("/my-snacks")
    @PreAuthorize("hasRole('VENDOR')")
    @Operation(summary = "Get vendor's own snacks", description = "Returns snacks owned by the authenticated vendor")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vendor snacks retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - VENDOR role required")
    })
    public ResponseEntity<List<SnackResponse>> getMySnacks(Authentication authentication) {
        return ResponseEntity.ok(snackService.getSnacksByVendor(authentication.getName()));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('VENDOR', 'ADMIN')")
    @Operation(summary = "Create a new snack", description = "Creates a new vegan snack product")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Snack created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<SnackResponse> createSnack(@Valid @RequestBody SnackRequest request,
                                                     Authentication authentication) {
        SnackResponse response = snackService.createSnack(request, authentication.getName());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('VENDOR', 'PRODUCT_MANAGER', 'ADMIN')")
    @Operation(summary = "Update a snack", description = "Updates an existing snack. Vendors can only update their own snacks.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Snack updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Snack not found")
    })
    public ResponseEntity<SnackResponse> updateSnack(@PathVariable Long id,
                                                     @Valid @RequestBody SnackRequest request,
                                                     Authentication authentication) {
        boolean isAdminOrManager = isAdminOrManager(authentication);
        SnackResponse response = snackService.updateSnack(id, request, authentication.getName(), isAdminOrManager);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('VENDOR', 'ADMIN')")
    @Operation(summary = "Delete a snack", description = "Deletes a snack. Vendors can only delete their own snacks.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Snack deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Snack not found")
    })
    public ResponseEntity<Map<String, String>> deleteSnack(@PathVariable Long id,
                                                           Authentication authentication) {
        boolean isAdminOrManager = isAdminOrManager(authentication);
        snackService.deleteSnack(id, authentication.getName(), isAdminOrManager);
        return ResponseEntity.ok(Map.of("message", "Snack deleted successfully."));
    }

    @PatchMapping("/{id}/approve")
    @PreAuthorize("hasAnyRole('PRODUCT_MANAGER', 'ADMIN')")
    @Operation(summary = "Approve or reject a snack", description = "Updates the approval status of a snack product")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Snack approval status updated"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - PRODUCT_MANAGER or ADMIN role required"),
            @ApiResponse(responseCode = "404", description = "Snack not found")
    })
    public ResponseEntity<SnackResponse> approveSnack(@PathVariable Long id,
                                                      @RequestParam boolean approved,
                                                      Authentication authentication) {
        SnackResponse response = snackService.approveSnack(id, authentication.getName(), approved);
        return ResponseEntity.ok(response);
    }

    private boolean isAdminOrManager(Authentication authentication) {
        return authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))
                || authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_PRODUCT_MANAGER"));
    }
}
