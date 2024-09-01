package com.exampleGroup.demo.service.impl;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.cglib.core.Signature;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import com.exampleGroup.demo.model.License;
import com.exampleGroup.demo.model.LicenseSignature;
import com.exampleGroup.demo.service.LicenseService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class LicenseServiceImplTest {
    private LicenseService licenseService;
    private PublicKey publicKey;
    private PrivateKey privateKey;

    @BeforeEach
    void setUp(){
        licenseService = new LicenseServiceImpl();
        KeyPair keyPair = licenseService.generateECKeyPair();
        this.publicKey = keyPair.getPublic();
        this.privateKey = keyPair.getPrivate();
    }

    @Test
    void testValidLicense(){
        License license = this.createLicenseWithSignature();
        boolean isValid = licenseService.isValidLicense(license);
        assertTrue(isValid, "Test for valid license");
    }

    @Test
    void testInValidLicense(){
        License license = this.createLicenseWithSignature();
        boolean isValid = licenseService.isValidLicense(license);
        assertFalse(isValid, "Test for invalid license");
    }

    private static ObjectMapper objectMapper = new ObjectMapper();
    private LicenseSignature createSignature(boolean isValid){
        LicenseSignature signature = new LicenseSignature();
        String encodedPublicKey = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        signature.setPublicKey(encodedPublicKey);
        // TODO: replace byte to a valid byte
        String base64Encoded = "U29tZSBiaW5hcnkgZGF0YSB0byBkZWNvZGU=";
        signature.setBytes(base64Encoded);
        return signature;
    }
    private License createLicense(LicenseSignature signature){
        License license = new License();
        license.setName("Test license");
        license.setExpired("2025-12-31");
        // Signature signature = new Signature();
        license.setLicenseSignature(signature);
        return license;
    }
    private License createLicenseWithSignature(){
        try{
        LicenseSignature signature = this.createSignature(false);
        License license = this.createLicense(null);
        String jsonPayloadStr = objectMapper.writeValueAsString(license);
        String signedByte = this.licenseService.signLicense(this.privateKey, jsonPayloadStr);
        signature.setBytes(signedByte);
        license.setLicenseSignature(signature);
        return license;

        } catch (JsonProcessingException e) {
            // Handle JSON processing errors
            System.err.println("Error: Failed to process JSON - " + e.getMessage());
        } catch (Exception e) {
            // Catch any other unexpected errors
            System.err.println("Error: An unexpected error occurred - " + e.getMessage());
        }
        return null;
    }

}
