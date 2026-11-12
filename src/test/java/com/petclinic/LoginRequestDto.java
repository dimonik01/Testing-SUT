package com.petclinic;

public class LoginRequestDto {
    private final String login;
    private final String password = "123456";
    private final String providerType;
    private final String applicationType;

    public LoginRequestDto(String login, String providerType, String applicationType){
        this.login = login;
        this.providerType = providerType;
        this.applicationType = applicationType;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getApplicationType() {
        return applicationType;
    }

    public String getProviderType() {
        return providerType;
    }
}
