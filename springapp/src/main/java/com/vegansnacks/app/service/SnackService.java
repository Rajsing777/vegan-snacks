package com.vegansnacks.app.service;

import com.vegansnacks.app.dto.SnackRequest;
import com.vegansnacks.app.dto.SnackResponse;
import com.vegansnacks.app.entity.SnackStatus;
import com.vegansnacks.app.entity.User;
import com.vegansnacks.app.entity.VeganSnackEntity;
import com.vegansnacks.app.repository.SnackRepository;
import com.vegansnacks.app.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Snack service implementing full CRUD with role-based scoping.
 * Vendors can only manage their own snacks; Admin/PM can manage all.
 */
@Service
public class SnackService {

    private final SnackRepository snackRepository;
    private final UserRepository userRepository;

    public SnackService(SnackRepository snackRepository, UserRepository userRepository) {
        this.snackRepository = snackRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public SnackResponse createSnack(SnackRequest request, String username) {
        User vendor = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + username));

        if (request.getSku() != null && snackRepository.existsBySku(request.getSku())) {
            throw new IllegalArgumentException("SKU '" + request.getSku() + "' already exists.");
        }

        VeganSnackEntity snack = new VeganSnackEntity();
        snack.setVendor(vendor);
        snack.setSnackName(request.getSnackName());
        snack.setSnackType(request.getSnackType());
        snack.setDescription(request.getDescription());
        snack.setIngredients(request.getIngredients());
        snack.setNutritionalInfo(request.getNutritionalInfo());
        snack.setQuantity(request.getQuantity());
        snack.setPrice(request.getPrice());
        snack.setExpiryInMonths(request.getExpiryInMonths());
        snack.setSku(request.getSku());
        snack.setStatus(SnackStatus.DRAFT);

        VeganSnackEntity saved = snackRepository.save(snack);
        return mapToResponse(saved);
    }

    public List<SnackResponse> getAllSnacks() {
        return snackRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<SnackResponse> getSnacksByVendor(String username) {
        User vendor = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + username));
        return snackRepository.findByVendorId(vendor.getId())
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public SnackResponse getSnackById(Long id) {
        VeganSnackEntity snack = snackRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Snack not found with id: " + id));
        return mapToResponse(snack);
    }

    @Transactional
    public SnackResponse updateSnack(Long id, SnackRequest request, String username, boolean isAdminOrManager) {
        VeganSnackEntity snack = snackRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Snack not found with id: " + id));

        if (!isAdminOrManager && !snack.getVendor().getUsername().equals(username)) {
            throw new AccessDeniedException("You can only modify your own snacks.");
        }

        snack.setSnackName(request.getSnackName());
        snack.setSnackType(request.getSnackType());
        snack.setDescription(request.getDescription());
        snack.setIngredients(request.getIngredients());
        snack.setNutritionalInfo(request.getNutritionalInfo());
        snack.setQuantity(request.getQuantity());
        snack.setPrice(request.getPrice());
        snack.setExpiryInMonths(request.getExpiryInMonths());
        if (request.getSku() != null) {
            snack.setSku(request.getSku());
        }

        VeganSnackEntity updated = snackRepository.save(snack);
        return mapToResponse(updated);
    }

    @Transactional
    public void deleteSnack(Long id, String username, boolean isAdminOrManager) {
        VeganSnackEntity snack = snackRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Snack not found with id: " + id));

        if (!isAdminOrManager && !snack.getVendor().getUsername().equals(username)) {
            throw new AccessDeniedException("You can only delete your own snacks.");
        }

        snackRepository.delete(snack);
    }

    @Transactional
    public SnackResponse approveSnack(Long id, String approverUsername, boolean approved) {
        VeganSnackEntity snack = snackRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Snack not found with id: " + id));

        User approver = userRepository.findByUsername(approverUsername)
                .orElseThrow(() -> new EntityNotFoundException("Approver not found: " + approverUsername));

        snack.setStatus(approved ? SnackStatus.APPROVED : SnackStatus.REJECTED);
        snack.setApprovedBy(approver);
        snack.setApprovalDate(LocalDateTime.now());

        VeganSnackEntity updated = snackRepository.save(snack);
        return mapToResponse(updated);
    }

    private SnackResponse mapToResponse(VeganSnackEntity entity) {
        SnackResponse response = new SnackResponse();
        response.setId(entity.getId());
        response.setVendorId(entity.getVendor() != null ? entity.getVendor().getId() : null);
        response.setVendorName(entity.getVendor() != null ? entity.getVendor().getUsername() : null);
        response.setSnackName(entity.getSnackName());
        response.setSnackType(entity.getSnackType());
        response.setDescription(entity.getDescription());
        response.setIngredients(entity.getIngredients());
        response.setNutritionalInfo(entity.getNutritionalInfo());
        response.setQuantity(entity.getQuantity());
        response.setPrice(entity.getPrice());
        response.setExpiryInMonths(entity.getExpiryInMonths());
        response.setSku(entity.getSku());
        response.setStatus(entity.getStatus() != null ? entity.getStatus().name() : null);
        response.setCreatedDate(entity.getCreatedDate());
        response.setLastModified(entity.getLastModified());
        response.setApprovedById(entity.getApprovedBy() != null ? entity.getApprovedBy().getId() : null);
        response.setApprovalDate(entity.getApprovalDate());
        return response;
    }
}
