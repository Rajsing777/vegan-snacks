package com.examly.springapp.controller;

import com.examly.springapp.exception.InvalidExpiryException;
import com.examly.springapp.model.VeganSnack;
import com.examly.springapp.service.VeganSnackService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class VeganSnackController {

    private final VeganSnackService veganSnackService;

    public VeganSnackController(VeganSnackService veganSnackService) {
        this.veganSnackService = veganSnackService;
    }

    @PostMapping("/addVeganSnack")
    public ResponseEntity<?> addVeganSnack(@RequestBody VeganSnack veganSnack) {
        return ResponseEntity.ok(veganSnackService.addVeganSnack(veganSnack));
    }

    @GetMapping("/getAllVeganSnacks")
    public ResponseEntity<List<VeganSnack>> getAllVeganSnacks() {
        return ResponseEntity.ok(veganSnackService.getAllVeganSnacks());
    }

    @ExceptionHandler(InvalidExpiryException.class)
    public ResponseEntity<String> handleInvalidExpiry(InvalidExpiryException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
