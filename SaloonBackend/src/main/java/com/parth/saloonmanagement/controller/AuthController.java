package com.parth.saloonmanagement.controller;

import com.parth.saloonmanagement.dto.AuthResponse;
import com.parth.saloonmanagement.dto.CustomerSignupRequest;
import com.parth.saloonmanagement.dto.LoginRequest;
import com.parth.saloonmanagement.dto.SignUpRequest;
import com.parth.saloonmanagement.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService  = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signUp(@Valid @RequestBody SignUpRequest request){
        AuthResponse response = authService.signUp(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request){
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);

    }

    @PostMapping("/customer-signup")
    public ResponseEntity<AuthResponse> signUp(@Valid @RequestBody CustomerSignupRequest request){
        AuthResponse response = authService.customerSignUp(request);
        return ResponseEntity.ok(response);
    }
}
