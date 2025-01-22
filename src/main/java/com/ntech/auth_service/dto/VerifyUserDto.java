package com.ntech.auth_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyUserDto {
    @NotBlank(message = "Email must not be blank")
    @Email(message = "Email should be in a valid format")
    private String email;

    @NotBlank(message = "Verification code must not be blank")
    private String verificationCode;
}
