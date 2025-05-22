package com.antmen.antwork;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HellowController {

    @GetMapping("/api/v1")
    public String hello() {
        return "Hello, World!";
    }
}
