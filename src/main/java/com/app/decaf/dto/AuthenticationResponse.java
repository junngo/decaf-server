package com.app.decaf.dto;

public class AuthenticationResponse {
    private final String jwt;

    public AuthenticationResponse(String jwt) {
        this.jwt = jwt;
    }

    // Getters and setters
    public String getJwt() {
        return jwt;
    }
}
