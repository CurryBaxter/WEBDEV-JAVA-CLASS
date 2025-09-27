package com.customer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TestController {

    @GetMapping("/test-validation")
    public String testValidation() {
        return "test-validation";
    }
    
    @PostMapping("/test-force-error")
    public String forceError(@RequestParam(required = false) String errorType) {
        switch (errorType != null ? errorType : "generic") {
            case "illegal-argument":
                throw new IllegalArgumentException("Dies ist eine Test-IllegalArgumentException zur Demonstration von Debug-Informationen");
            case "null-pointer":
                throw new NullPointerException("Dies ist eine Test-NullPointerException zur Demonstration von Debug-Informationen");
            case "validation":
                throw new IllegalArgumentException("Erzwungener Validierungsfehler zur Demonstration von Debug-Informationen");
            default:
                throw new RuntimeException("Dies ist eine generische Testausnahme zur Demonstration von Debug-Informationen");
        }
    }
}