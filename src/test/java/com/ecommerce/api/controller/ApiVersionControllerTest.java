package com.ecommerce.api.controller;

import com.ecommerce.api.config.ApiVersionConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * API version controller test
 */
@WebMvcTest(ApiVersionController.class)
class ApiVersionControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    void testGetVersionInfo() throws Exception {
        mockMvc.perform(get("/api/version/info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.currentVersion").value(ApiVersionConfig.DEFAULT_VERSION))
                .andExpect(jsonPath("$.data.supportedVersions").isArray())
                .andExpect(jsonPath("$.data.strategy.urlPathVersioning").value(true))
                .andExpect(jsonPath("$.data.compatibility.backwardCompatible").value(true));
    }
    
    @Test
    void testCheckVersionCompatibility_SupportedVersion() throws Exception {
        mockMvc.perform(get("/api/version/compatibility/v1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.requestedVersion").value("v1"))
                .andExpect(jsonPath("$.data.isSupported").value(true))
                .andExpect(jsonPath("$.data.currentVersion").value(ApiVersionConfig.DEFAULT_VERSION));
    }
    
    @Test
    void testCheckVersionCompatibility_UnsupportedVersion() throws Exception {
        mockMvc.perform(get("/api/version/compatibility/v99"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.requestedVersion").value("v99"))
                .andExpect(jsonPath("$.data.isSupported").value(false))
                .andExpect(jsonPath("$.data.recommendation").exists());
    }
    
    @Test
    void testCheckVersionCompatibility_WithoutVPrefix() throws Exception {
        mockMvc.perform(get("/api/version/compatibility/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.requestedVersion").value("v1"))
                .andExpect(jsonPath("$.data.isSupported").value(true));
    }
        
    @Test
    void testCheckVersionCompatibility_EmptyString() throws Exception {
        mockMvc.perform(get("/api/version/compatibility/"))
                .andExpect(status().isNotFound()); 
    }
    
    @Test
    void testCheckVersionCompatibility_NonNumericVersion() throws Exception {
        mockMvc.perform(get("/api/version/compatibility/abc"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("Invalid version format. Version must be a positive integer (e.g., 'v1', 'v2')"));
    }
    
    @Test
    void testCheckVersionCompatibility_NonNumericVersionWithVPrefix() throws Exception {
        mockMvc.perform(get("/api/version/compatibility/vabc"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("Invalid version format. Version must be a positive integer (e.g., 'v1', 'v2')"));
    }
    
    @Test
    void testCheckVersionCompatibility_DecimalVersion() throws Exception {
        mockMvc.perform(get("/api/version/compatibility/v1.0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("Invalid version format. Version must be a positive integer (e.g., 'v1', 'v2')"));
    }
    
    @Test
    void testCheckVersionCompatibility_DecimalVersionWithoutVPrefix() throws Exception {
        mockMvc.perform(get("/api/version/compatibility/1.0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("Invalid version format. Version must be a positive integer (e.g., 'v1', 'v2')"));
    }
    
    @Test
    void testCheckVersionCompatibility_VersionWithSpecialChars() throws Exception {
        mockMvc.perform(get("/api/version/compatibility/v1-rc"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("Invalid version format. Version must be a positive integer (e.g., 'v1', 'v2')"));
    }
    
    @Test
    void testCheckVersionCompatibility_VersionWithSpecialCharsWithoutVPrefix() throws Exception {
        mockMvc.perform(get("/api/version/compatibility/1-rc"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("Invalid version format. Version must be a positive integer (e.g., 'v1', 'v2')"));
    }
    
    @Test
    void testCheckVersionCompatibility_SupportedVersionV2() throws Exception {
        mockMvc.perform(get("/api/version/compatibility/v2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.requestedVersion").value("v2"))
                .andExpect(jsonPath("$.data.isSupported").value(true))
                .andExpect(jsonPath("$.data.currentVersion").value(ApiVersionConfig.DEFAULT_VERSION));
    }
    
    @Test
    void testCheckVersionCompatibility_SupportedVersionV2WithoutVPrefix() throws Exception {
        mockMvc.perform(get("/api/version/compatibility/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.requestedVersion").value("v2"))
                .andExpect(jsonPath("$.data.isSupported").value(true))
                .andExpect(jsonPath("$.data.currentVersion").value(ApiVersionConfig.DEFAULT_VERSION));
    }

    @Test
    void testCheckVersionCompatibility_EmptyStringParameter() throws Exception {
        // This test covers the case where an empty string is passed as a path parameter
        // Note: This is different from the empty path test above which returns 404
        mockMvc.perform(get("/api/version/compatibility/ "))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("Version parameter cannot be null or empty"));
    }

    @Test
    void testCheckVersionCompatibility_ZeroVersion() throws Exception {
        mockMvc.perform(get("/api/version/compatibility/0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.requestedVersion").value("v0"))
                .andExpect(jsonPath("$.data.isSupported").value(false))
                .andExpect(jsonPath("$.data.recommendation").exists());
    }

    @Test
    void testCheckVersionCompatibility_NegativeVersion() throws Exception {
        mockMvc.perform(get("/api/version/compatibility/-1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("Invalid version format. Version must be a positive integer (e.g., 'v1', 'v2')"));
    }
} 