package com.ntech.auth_service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterUserDto {
    private String username;
    private String password;
    private String email;

    public void setEmail(String email) {
        this.email = email;
        if(email != null && email.contains("@") && username != null && !username.isEmpty()) {
            this.username = email.split("@")[0];
        }
    }
}
