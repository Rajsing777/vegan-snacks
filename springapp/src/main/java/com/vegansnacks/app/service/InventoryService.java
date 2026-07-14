package com.vegansnacks.app.service;

import com.vegansnacks.app.dto.InventoryResponse;
import com.vegansnacks.app.dto.InventoryUpdateRequest;
import com.vegansnacks.app.entity.Inventory;
import com.vegansnacks.app.entity.VeganSnackEntity;
import com.vegansnacks.app.repository.InventoryRepository;
import com.vegansnacks.app.repository.SnackRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Inventory service for stock tracking, updates, and threshold alerts.
 */
@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final SnackRepository snackRepository;

    public InventoryService(InventoryRepository inventoryRepository, SnackRepository snackRepository) {
        this.inventoryRepository = inventoryRepository;
        this.snackRepository = snackRepository;
    }

    public List<InventoryResponse> getAllInventory() {
        return inventoryRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public InventoryResponse getInventoryByProductId(Long productId) {
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new EntityNotFoundException("Inventory not found for product id: " + productId));
        return mapToResponse(inventory);
    }

    @Transactional
    public InventoryResponse createInventory(Long productId, InventoryUpdateRequest request) {
        VeganSnackEntity product = snackRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + productId));

        if (inventoryRepository.findByProductId(productId).isPresent()) {
            throw new IllegalArgumentException("Inventory already exists for product id: " + productId);
        }

        Inventory inventory = new Inventory();
        inventory.setProduct(product);
        inventory.setCurrentStock(request.getCurrentStock());
        inventory.setReorderPoint(request.getReorderPoint() != null ? request.getReorderPoint() : 10);
        inventory.setMaxStock(request.getMaxStock() != null ? request.getMaxStock() : 1000);
        inventory.setCostPerUnit(request.getCostPerUnit());
        inventory.setLocation(request.getLocation());
        inventory.setLastRestockDate(LocalDateTime.now());

        Inventory saved = inventoryRepository.save(inventory);
        return mapToResponse(saved);
    }

    @Transactional
    public InventoryResponse updateStock(Long productId, InventoryUpdateRequest request) {
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new EntityNotFoundException("Inventory not found for product id: " + productId));

        if (request.getCurrentStock() != null) {
            inventory.setCurrentStock(request.getCurrentStock());
            inventory.setLastRestockDate(LocalDateTime.now());
        }
        if (request.getReorderPoint() != null) {
            inventory.setReorderPoint(request.getReorderPoint());
        }
        if (request.getMaxStock() != null) {
            inventory.setMaxStock(request.getMaxStock());
        }
        if (request.getCostPerUnit() != null) {
            inventory.setCostPerUnit(request.getCostPerUnit());
        }
        if (request.getLocation() != null) {
            inventory.setLocation(request.getLocation());
        }

        Inventory updated = inventoryRepository.save(inventory);
        return mapToResponse(updated);
    }

    public List<InventoryResponse> getLowStockAlerts() {
        return inventoryRepository.findLowStockItems()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<InventoryResponse> getOutOfStockAlerts() {
        return inventoryRepository.findOutOfStockItems()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private InventoryResponse mapToResponse(Inventory entity) {
        InventoryResponse response = new InventoryResponse();
        response.setId(entity.getId());
        response.setProductId(entity.getProduct() != null ? entity.getProduct().getId() : null);
        response.setProductName(entity.getProduct() != null ? entity.getProduct().getSnackName() : null);
        response.setCurrentStock(entity.getCurrentStock());
        response.setReorderPoint(entity.getReorderPoint());
        response.setMaxStock(entity.getMaxStock());
        response.setCostPerUnit(entity.getCostPerUnit());
        response.setLastRestockDate(entity.getLastRestockDate());
        response.setLastUpdated(entity.getLastUpdated());
        response.setLocation(entity.getLocation());
        response.setLowStock(entity.getCurrentStock() <= entity.getReorderPoint());
        response.setOutOfStock(entity.getCurrentStock() == 0);
        return response;
    }
}
