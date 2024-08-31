package com.exampleGroup.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.exampleGroup.demo.model.License;

@RestController
@RequestMapping("/api")
public class LicenseController {


    @PostMapping("/validate-license")
    public ResponseEntity<String> validateLicense(@RequestBody License license) {

        return new ResponseEntity<>("License is valid", HttpStatus.OK);
    }
}
