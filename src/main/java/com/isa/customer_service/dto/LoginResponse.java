package com.isa.customer_service.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoginResponse {
    private String token;
    private long expiresIn;  // This should be of type long (or Date if you're using that type)
    private String refreshToken;
    public LoginResponse setToken(String token) {
        this.token = token;
        return this;
    }
    public LoginResponse setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    public LoginResponse setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
        return this;
    }
}

