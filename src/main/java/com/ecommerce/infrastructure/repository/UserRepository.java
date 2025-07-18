package com.ecommerce.infrastructure.repository;

import com.ecommerce.domain.user.User;
import java.util.Optional;
import java.util.Set;

/**
 * User Repository Interface
 * Defines data access contract for User aggregate
 */
public interface UserRepository {
    
    /**
     * Save user
     */
    User save(User user);
    
    /**
     * Find user by ID
     */
    Optional<User> findById(Long id);
    
    /**
     * Check if user exists by ID
     */
    boolean existsById(Long id);
    
    /**
     * Find user by phone number
     */
    Optional<User> findByPhone(String phone);
    
    /**
     * Find user by email
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Check if user exists by phone number
     */
    boolean existsByPhone(String phone);
    
    /**
     * Check if user exists by email
     */
    boolean existsByEmail(String email);
    
    /**
     * Get all user IDs
     */
    Set<Long> getAllUserIds();
    
    /**
     * Get total user count
     */
    int count();
    
    /**
     * Delete user by ID
     */
    void deleteById(Long id);
} 