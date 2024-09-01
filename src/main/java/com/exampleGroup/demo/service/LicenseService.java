package com.exampleGroup.demo.service;

import java.security.KeyPair;
import java.security.PrivateKey;

import com.exampleGroup.demo.model.License;

public interface LicenseService {
    public boolean isValidLicense(License license);
    public KeyPair generateECKeyPair();
    public String signLicense(PrivateKey privateKey, String payload);
}
