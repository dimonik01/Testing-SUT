package com.petclinic;

public class AuthRequestDto {
    private final String smsOtpCode;

    public AuthRequestDto(){
        this.smsOtpCode = "123456";
    }
}
