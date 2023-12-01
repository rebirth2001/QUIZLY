package com.example.app.controller;

import com.example.app.http.AuthenticationRequest;
import com.example.app.http.AuthenticationResponse;
import com.example.app.service.AuthenticationService;
import com.example.app.http.RegisterRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService service;
    @PostMapping("/sign-up")
    public ResponseEntity<AuthenticationResponse> signUp(
            @RequestBody @Valid RegisterRequest request
    ) {
        var resp  = service.register(request);
        if(resp.isError()){
            return ResponseEntity.badRequest().body(resp);
        }else{
            return ResponseEntity.ok(resp);
        }
    }

    @PostMapping("/sign-in")
    public ResponseEntity<AuthenticationResponse> signIn(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @PostMapping("/sign-out")
    public ResponseEntity<AuthenticationResponse> signOut(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }

}