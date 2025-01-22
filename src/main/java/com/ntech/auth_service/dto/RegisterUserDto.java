package com.ntech.auth_service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterUserDto {
    private String username;
    private String password;
    private String email;

//    Uncomment this when username is not a mandatory field at the frontend
//    public void setEmail(String email) {
//        this.email = email;
//        if(username != null && !username.isEmpty() && email != null && email.contains("@")) {
//            this.username = email.split("@")[0];
//        }
//    }
}
