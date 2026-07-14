package com.examly.springapp.service;

import com.examly.springapp.exception.InvalidExpiryException;
import com.examly.springapp.model.VeganSnack;
import com.examly.springapp.repository.VeganSnackRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VeganSnackService {

    private final VeganSnackRepo veganSnackRepo;

    public VeganSnackService(VeganSnackRepo veganSnackRepo) {
        this.veganSnackRepo = veganSnackRepo;
    }

    public VeganSnack addVeganSnack(VeganSnack veganSnack) {
        int expiry = Integer.parseInt(veganSnack.getExpiryInMonths());
        if (expiry < 0) {
            throw new InvalidExpiryException("Expiry in months should not be a negative value.");
        }
        return veganSnackRepo.save(veganSnack);
    }

    public List<VeganSnack> getAllVeganSnacks() {
        return veganSnackRepo.findAll();
    }
}
