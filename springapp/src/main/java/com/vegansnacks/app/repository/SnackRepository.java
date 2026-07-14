package com.vegansnacks.app.repository;

import com.vegansnacks.app.entity.VeganSnackEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SnackRepository extends JpaRepository<VeganSnackEntity, Long> {

    List<VeganSnackEntity> findByVendorId(Long vendorId);

    Optional<VeganSnackEntity> findBySku(String sku);

    boolean existsBySku(String sku);

    List<VeganSnackEntity> findByStatus(com.vegansnacks.app.entity.SnackStatus status);
}
