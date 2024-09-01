package com.exampleGroup.demo.model;

import lombok.Data;

@Data
public class LicenseSignature {

    private String publicKey;
    private String curve;
    private String bytes;  // Assuming this is a base64-encoded string
    private String algorithm;
}