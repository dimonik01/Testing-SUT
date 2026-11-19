package com.petclinic;

public class AuthRequestDto {
    private final String smsOtpCode;

    public AuthRequestDto(){
        this.smsOtpCode = "123456";
    }

    public String getSmsOtpCode() {
        return smsOtpCode;
    }
}
