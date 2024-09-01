package com.exampleGroup.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.exampleGroup.demo.model.License;
import com.exampleGroup.demo.service.LicenseService;

@RestController
@RequestMapping("/api")
public class LicenseController {

    @Autowired
    private LicenseService licenseService;

    @PostMapping("/validate-license")
    public ResponseEntity<String> validateLicense(@RequestBody License license) {

        boolean isValid = this.licenseService.isValidLicense(license);
        if(isValid){
            return new ResponseEntity<>("License is valid", HttpStatus.OK);
        }else{
            return new ResponseEntity<>("License is invalid", HttpStatus.UNAUTHORIZED);
        }
    }
}
