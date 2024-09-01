package com.exampleGroup.demo.service.impl;

import com.exampleGroup.demo.model.License;
import com.exampleGroup.demo.service.LicenseService;
import org.springframework.stereotype.Service;

import java.security.*;
import java.security.spec.*;
import java.util.Base64;

@Service
public class LicenseServiceImpl implements LicenseService{

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

            // Get the data to verify (e.g., combine fields like name, info, lists, expired)
            String dataToVerify = license.getName() + license.getExpired();
            // Add more fields as necessary
            byte[] dataBytes = dataToVerify.getBytes();
            signature.update(dataBytes);

            // Decode the signature bytes
            byte[] signatureBytes = Base64.getDecoder().decode(license.getLicenseSignature().getBytes());

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
            // PublicKey publicKey = keyPair.getPublic();
            // // String encodedPublicKey = Base64.getEncoder().encodeToString(publicKey.getEncoded());
            // PrivateKey privateKey = keyPair.getPrivate();
            // // String encodedPrivateKey = Base64.getEncoder().encodeToString(privateKey.getEncoded());
            // return new ECKeyPair(publicKey, privateKey);
        }catch(NoSuchAlgorithmException | java.security.InvalidAlgorithmParameterException e){
            e.printStackTrace();
            return null;
        }
    }

    public String signLicense(PrivateKey privateKey, String payload){
        try{
            Signature signature = Signature.getInstance("SHA256withECDSA");
            signature.initSign(privateKey);
            signature.update(payload.getBytes());
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
