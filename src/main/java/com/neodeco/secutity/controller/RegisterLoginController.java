package com.neodeco.secutity.controller;


import com.neodeco.secutity.model.AuthRequest;
import com.neodeco.secutity.model.AuthResponse;
import com.neodeco.secutity.model.RegisterRequest;
import com.neodeco.secutity.service.impl.AuthServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class RegisterLoginController {
    private final AuthServiceImpl service;
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(service.register(request));
    }
    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponse> authtenticate(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }
    @GetMapping("/greetings")
    public ResponseEntity<String> greet() {
        return ResponseEntity.ok("Hello there! You're authenticated!");
    }
}
