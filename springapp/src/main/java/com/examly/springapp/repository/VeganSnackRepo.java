package com.examly.springapp.repository;

import com.examly.springapp.model.VeganSnack;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VeganSnackRepo extends JpaRepository<VeganSnack, Long> {
}
