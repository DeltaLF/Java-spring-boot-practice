package com.exampleGroup.demo.service.impl;

import com.exampleGroup.demo.model.License;
import com.exampleGroup.demo.model.LicenseSignature;
import com.exampleGroup.demo.service.LicenseService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Service;

import java.security.*;
import java.security.spec.*;
import java.util.Base64;

@Service
public class LicenseServiceImpl implements LicenseService{

    private static ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public boolean isValidLicense(License license) {
        try{
            byte[] publicKeyBytes = Base64.getDecoder().decode(license.getLicenseSignature().getPublicKey());
            KeyFactory keyFactory = KeyFactory.getInstance("EC");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
            PublicKey publicKey = keyFactory.generatePublic(keySpec);

            // Prepare the Signature object
            Signature signature = Signature.getInstance("SHA256withECDSA");
            signature.initVerify(publicKey);

            LicenseSignature licenseSignature = license.getLicenseSignature();
            license.setLicenseSignature(null);
            String dataToVerify;
            try{
                dataToVerify = objectMapper.writeValueAsString(license);

            }catch (JsonProcessingException e){
                System.err.println("Error: License json is invalid - " + e.getMessage());
                return false;
            }
            // Add more fields as necessary
            byte[] dataBytes = dataToVerify.getBytes();
            signature.update(dataBytes);

            // Decode the signature bytes
            byte[] signatureBytes = Base64.getDecoder().decode(licenseSignature.getBytes());

            // Verify the signature
            return signature.verify(signatureBytes);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException | SignatureException e) {
            e.printStackTrace();
            return false;
        }
    }

    public KeyPair generateECKeyPair(){
        ECGenParameterSpec ecSpec = new ECGenParameterSpec("secp256r1");
        try{
            KeyPairGenerator g = KeyPairGenerator.getInstance("EC");
            KeyPair keyPair = g.generateKeyPair();
            g.initialize(ecSpec, new SecureRandom());
            return keyPair;
        }catch(NoSuchAlgorithmException | java.security.InvalidAlgorithmParameterException e){
            e.printStackTrace();
            return null;
        }
    }

    public String signLicense(PrivateKey privateKey, String payload){
        try{
            Signature signature = Signature.getInstance("SHA256withECDSA");
            signature.initSign(privateKey);
            signature.update(payload.getBytes("UTF-8"));
            byte[] signatureBytes = signature.sign();
            return Base64.getEncoder().encodeToString(signatureBytes);
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Error: The specified algorithm is not available - " + e.getMessage());
        } catch (InvalidKeyException e) {
            System.err.println("Error: The provided private key is invalid - " + e.getMessage());
        } catch (SignatureException e) {
            System.err.println("Error: An error occurred during the signing process - " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error: An unexpected error occurred - " + e.getMessage());
        }
        return null;
    }

}
