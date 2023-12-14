package com.example.app.login_and_registration.controller;

import com.example.app.login_and_registration.http.RegisterRequest;
import com.example.app.login_and_registration.http.RegistrationResponse;
import com.example.app.login_and_registration.http.UserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/details")
@RequiredArgsConstructor
public class UserDetailsController {
    @GetMapping("/")
    public ResponseEntity<UserDetails> signUp(
    ) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(auth.toString());
        var creds = auth.getCredentials();
        System.out.println(creds.toString());

        return ResponseEntity.ok(new UserDetails("test","test"));
    }
}
