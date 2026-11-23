package com.testing.api.dto;

public class AuthRequestDto {
    private final String smsOtpCode;

    public AuthRequestDto(String smsOtpCode){
        this.smsOtpCode = smsOtpCode;
    }

    public String getSmsOtpCode() {
        return smsOtpCode;
    }
}
