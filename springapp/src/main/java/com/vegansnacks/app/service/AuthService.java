package com.vegansnacks.app.service;

import com.vegansnacks.app.dto.AuthRequest;
import com.vegansnacks.app.dto.AuthResponse;
import com.vegansnacks.app.dto.RegisterRequest;
import com.vegansnacks.app.entity.Role;
import com.vegansnacks.app.entity.User;
import com.vegansnacks.app.repository.UserRepository;
import com.vegansnacks.app.security.JwtTokenUtil;
import com.vegansnacks.app.security.TokenBlacklistService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Authentication service handling user registration, login, logout, and token refresh.
 */
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;
    private final TokenBlacklistService tokenBlacklistService;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtTokenUtil jwtTokenUtil,
                       AuthenticationManager authenticationManager,
                       TokenBlacklistService tokenBlacklistService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenUtil = jwtTokenUtil;
        this.authenticationManager = authenticationManager;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username '" + request.getUsername() + "' is already taken.");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email '" + request.getEmail() + "' is already registered.");
        }

        Role role;
        try {
            role = Role.valueOf(request.getRole().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid role: " + request.getRole() +
                    ". Valid roles are: VENDOR, PRODUCT_MANAGER, ADMIN");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(role);
        user.setPhoneNumber(request.getPhoneNumber());
        user.setIsActive(true);
        user.setEmailVerified(false);

        User savedUser = userRepository.save(user);

        String accessToken = jwtTokenUtil.generateAccessToken(savedUser.getUsername(), savedUser.getRole().name());
        String refreshToken = jwtTokenUtil.generateRefreshToken(savedUser.getUsername());

        return new AuthResponse(
                accessToken,
                refreshToken,
                jwtTokenUtil.getAccessTokenExpirationMs() / 1000,
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getRole().name()
        );
    }

    public AuthResponse login(AuthRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password."));

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), request.getPassword()));

        // Update last login timestamp
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        String accessToken = jwtTokenUtil.generateAccessToken(user.getUsername(), user.getRole().name());
        String refreshToken = jwtTokenUtil.generateRefreshToken(user.getUsername());

        return new AuthResponse(
                accessToken,
                refreshToken,
                jwtTokenUtil.getAccessTokenExpirationMs() / 1000,
                user.getUsername(),
                user.getEmail(),
                user.getRole().name()
        );
    }

    public void logout(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        if (token != null) {
            tokenBlacklistService.blacklistToken(token);
        }
    }

    public AuthResponse refreshToken(String refreshTokenHeader) {
        String refreshToken = refreshTokenHeader;
        if (refreshToken != null && refreshToken.startsWith("Bearer ")) {
            refreshToken = refreshToken.substring(7);
        }

        if (refreshToken == null || tokenBlacklistService.isTokenBlacklisted(refreshToken)) {
            throw new BadCredentialsException("Invalid or expired refresh token.");
        }

        String tokenType = jwtTokenUtil.extractTokenType(refreshToken);
        if (!"refresh".equals(tokenType)) {
            throw new BadCredentialsException("Token is not a refresh token.");
        }

        String username = jwtTokenUtil.extractUsername(refreshToken);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + username));

        if (jwtTokenUtil.isTokenExpired(refreshToken)) {
            throw new BadCredentialsException("Refresh token has expired. Please login again.");
        }

        // Blacklist old refresh token and issue new pair
        tokenBlacklistService.blacklistToken(refreshToken);

        String newAccessToken = jwtTokenUtil.generateAccessToken(user.getUsername(), user.getRole().name());
        String newRefreshToken = jwtTokenUtil.generateRefreshToken(user.getUsername());

        return new AuthResponse(
                newAccessToken,
                newRefreshToken,
                jwtTokenUtil.getAccessTokenExpirationMs() / 1000,
                user.getUsername(),
                user.getEmail(),
                user.getRole().name()
        );
    }
}
