package com.example.app.login_and_registration.http;

import jakarta.validation.constraints.NotBlank;

public class TokenRefreshRequest {
    @NotBlank
    private String refreshToken;

    public String getRefreshToken(){
        return refreshToken;
    }
}
