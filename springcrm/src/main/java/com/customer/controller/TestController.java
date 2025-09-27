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
                throw new IllegalArgumentException("This is a test IllegalArgumentException to demonstrate debug information");
            case "null-pointer":
                throw new NullPointerException("This is a test NullPointerException to demonstrate debug information");
            case "validation":
                throw new IllegalArgumentException("Forced validation error to demonstrate debug information");
            default:
                throw new RuntimeException("This is a generic test exception to demonstrate debug information");
        }
    }
}