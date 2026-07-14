package com.vegansnacks.app.controller;

import com.vegansnacks.app.dto.InventoryResponse;
import com.vegansnacks.app.dto.InventoryUpdateRequest;
import com.vegansnacks.app.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Inventory controller for stock level tracking, updates, and real-time threshold alerts.
 */
@RestController
@RequestMapping("/api/v1/inventory")
@Tag(name = "Inventory", description = "Inventory level tracking, stock updates, and alert management")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('VENDOR', 'PRODUCT_MANAGER', 'ADMIN')")
    @Operation(summary = "Get all inventory records", description = "Returns all inventory entries")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Inventory records retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<List<InventoryResponse>> getAllInventory() {
        return ResponseEntity.ok(inventoryService.getAllInventory());
    }

    @GetMapping("/product/{productId}")
    @PreAuthorize("hasAnyRole('VENDOR', 'PRODUCT_MANAGER', 'ADMIN')")
    @Operation(summary = "Get inventory by product ID", description = "Returns inventory details for a specific product")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Inventory retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Inventory not found for the given product")
    })
    public ResponseEntity<InventoryResponse> getInventoryByProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(inventoryService.getInventoryByProductId(productId));
    }

    @PostMapping("/product/{productId}")
    @PreAuthorize("hasAnyRole('VENDOR', 'ADMIN')")
    @Operation(summary = "Create inventory for a product", description = "Initializes inventory tracking for a product")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Inventory created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input or inventory already exists"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<InventoryResponse> createInventory(@PathVariable Long productId,
                                                              @Valid @RequestBody InventoryUpdateRequest request) {
        InventoryResponse response = inventoryService.createInventory(productId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/product/{productId}")
    @PreAuthorize("hasAnyRole('VENDOR', 'ADMIN')")
    @Operation(summary = "Update inventory stock", description = "Updates stock levels and configuration for a product's inventory")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Inventory updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Inventory not found")
    })
    public ResponseEntity<InventoryResponse> updateStock(@PathVariable Long productId,
                                                          @Valid @RequestBody InventoryUpdateRequest request) {
        return ResponseEntity.ok(inventoryService.updateStock(productId, request));
    }

    @GetMapping("/alerts/low-stock")
    @PreAuthorize("hasAnyRole('VENDOR', 'PRODUCT_MANAGER', 'ADMIN')")
    @Operation(summary = "Get low stock alerts", description = "Returns inventory items where stock is at or below the reorder point")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Low stock alerts retrieved"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<List<InventoryResponse>> getLowStockAlerts() {
        return ResponseEntity.ok(inventoryService.getLowStockAlerts());
    }

    @GetMapping("/alerts/out-of-stock")
    @PreAuthorize("hasAnyRole('VENDOR', 'PRODUCT_MANAGER', 'ADMIN')")
    @Operation(summary = "Get out of stock alerts", description = "Returns inventory items with zero stock")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Out of stock alerts retrieved"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<List<InventoryResponse>> getOutOfStockAlerts() {
        return ResponseEntity.ok(inventoryService.getOutOfStockAlerts());
    }
}
