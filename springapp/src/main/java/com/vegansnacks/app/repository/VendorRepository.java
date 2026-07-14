package com.vegansnacks.app.repository;

import com.vegansnacks.app.entity.Vendor;
import com.vegansnacks.app.entity.VendorStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VendorRepository extends JpaRepository<Vendor, Long> {

    Optional<Vendor> findByUserId(Long userId);

    boolean existsByBusinessLicenseNumber(String businessLicenseNumber);

    List<Vendor> findByApprovalStatus(VendorStatus status);
}
