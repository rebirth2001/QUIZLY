package com.example.app.login_and_registration.controller;


import com.example.app.login_and_registration.http.*;
import com.example.app.login_and_registration.model.RefreshToken;
import com.example.app.login_and_registration.service.AuthenticationService;
import com.example.app.login_and_registration.service.RefreshTokenService;
import com.example.app.login_and_registration.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authService;
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/sign-up")
    public ResponseEntity<RegistrationResponse> signUp(
            @RequestBody @Valid RegisterRequest request,
            BindingResult bindingResult
    ) {
        if(bindingResult.hasErrors()) {
            List<String> errors = new ArrayList<>();
            for(FieldError error:bindingResult.getFieldErrors()){
                errors.add(error.getDefaultMessage());
            }
            RegistrationResponse errorResponse = RegistrationResponse.builder()
                            .errors(errors)
                                    .isError(true)
                                            .build();
            return ResponseEntity.badRequest().body(errorResponse);
        }
        var resp = authService.register(request);
        if (resp.isError()){
            return ResponseEntity.badRequest().body(resp);
        } else {
            return ResponseEntity.ok(resp);
        }
    }

    @PostMapping("/sign-in")
    public ResponseEntity<AuthenticationResponse> signIn(
            @RequestBody @Valid AuthenticationRequest request,
            BindingResult bindingResult
    ) {
        if(bindingResult.hasErrors()) {
            List<String> errors = new ArrayList<>();
            for(FieldError error:bindingResult.getFieldErrors()){
                errors.add(error.getDefaultMessage());
            }
            AuthenticationResponse errorResponse = AuthenticationResponse.builder()
                    .errors(errors)
                    .isError(true)
                    .build();
            return ResponseEntity.badRequest().body(errorResponse);
        }

        var resp = authService.authenticate(request,refreshTokenService);
        if (resp.isError()){
            return ResponseEntity.badRequest().body(resp);
        } else {
            return ResponseEntity.ok(resp);
        }
    }

    @PostMapping("/sign-out")
    public ResponseEntity<AuthenticationResponse> signOut(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return ResponseEntity.badRequest().body(ex.getBindingResult().getFieldError().getDefaultMessage());
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponse>refreshToken(
            @RequestBody TokenRefreshRequest req
    ){
        List<String> errors = new ArrayList<>();
        String requestRefreshToken = req.getRefreshToken();
        var token = refreshTokenService.findByToken(requestRefreshToken);
        if(token.isEmpty()){
            errors.add("Token Not Found");
            return ResponseEntity.badRequest().body(
                    AuthenticationResponse.builder()
                    .isError(true)
                    .errors(errors)
                    .build());
        }
        if(refreshTokenService.isExpired(token.get())){
            errors.add("Expired Token");
            return ResponseEntity.badRequest().body(
                    AuthenticationResponse.builder()
                            .isError(true)
                            .errors(errors)
                            .build());
        }
        try{
            return token.map(RefreshToken::getUser)
                    .map(user -> {
                        String jwt = authService.generateJwtToken(user);
                        return ResponseEntity.ok(
                                AuthenticationResponse.builder()
                                        .isError(false)
                                        .accessToken(jwt)
                                        .refreshToken(token.get().getToken())
                                        .username(user.getUsername())
                                        .expiration(authService.getExpiration())
                                        .build()
                        );
                    })
                    .orElseThrow();

        }catch(Exception e){
            System.out.println(e);
            errors.add("Internal Server Error");
            return  ResponseEntity.badRequest().body(
                    AuthenticationResponse.builder()
                            .isError(true)
                            .errors(errors)
                            .build());

        }
    }

    @GetMapping("/details")
    public ResponseEntity<UserDetails> signUp(
    ) {
        String email = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        System.out.println(email);
        var user = userService.findByEmail(email);
        return user.map(value -> ResponseEntity.ok(new UserDetails(value.getUsername(), value.getEmail()))).orElseGet(() -> ResponseEntity.badRequest().build());
    }
}