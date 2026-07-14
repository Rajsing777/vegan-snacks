package com.vegansnacks.app.security;

import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory token blacklist service using a concurrent set.
 * Tokens are added on logout and checked during JWT filter validation.
 * In production, this should be backed by Redis for distributed deployments.
 */
@Service
public class TokenBlacklistService {

    private final Set<String> blacklistedTokens = ConcurrentHashMap.newKeySet();

    /**
     * Blacklists a token so it can no longer be used for authentication.
     *
     * @param token the JWT token to blacklist
     */
    public void blacklistToken(String token) {
        blacklistedTokens.add(token);
    }

    /**
     * Checks whether a token has been blacklisted.
     *
     * @param token the JWT token to check
     * @return true if the token is blacklisted, false otherwise
     */
    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }
}
