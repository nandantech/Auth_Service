package com.ntech.auth_service.responses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
    private String token;
    private long expiresIn;
    private String message;
    private boolean success;

    public LoginResponse(String token, long expiresIn) {
        this.token = token;
        this.expiresIn = expiresIn;
        this.success = true;
    }

    public LoginResponse(String message){
        this.message = message;
        this.success = false;
    }

}
