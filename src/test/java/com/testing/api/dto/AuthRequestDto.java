package com.testing.api.dto;

public class AuthRequestDto {
    private final String auth_code;

    public AuthRequestDto(String auth_code){
        this.auth_code = auth_code;
    }

    public String getAuth_code() {
        return auth_code;
    }
}
