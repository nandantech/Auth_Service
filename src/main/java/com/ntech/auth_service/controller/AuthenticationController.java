package com.ntech.auth_service.controller;

import com.ntech.auth_service.dto.LoginUserDto;
import com.ntech.auth_service.dto.ResetPasswordDto;
import com.ntech.auth_service.responses.LoginResponse;
import com.ntech.auth_service.dto.RegisterUserDto;
import com.ntech.auth_service.dto.VerifyUserDto;
import com.ntech.auth_service.model.User;
import com.ntech.auth_service.service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/auth")
@RestController
@Validated
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> register(@RequestBody RegisterUserDto registerUserDto) {
        try{
            User user = authenticationService.registerUser(registerUserDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/verify")
    public  ResponseEntity<?> verifyUser(@Valid @RequestBody VerifyUserDto verifyUserDto) {
        try{
             LoginResponse loginResponse = authenticationService.verifyUser(verifyUserDto);
             if(!loginResponse.isSuccess()){
                 return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(loginResponse);
             }
            return ResponseEntity.status(HttpStatus.OK).body(loginResponse);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginUserDto loginUserDto) {
        try{
            LoginResponse loginResponse = authenticationService.authenticate(loginUserDto);
            if(!loginResponse.isSuccess()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(loginResponse);
            }
            return ResponseEntity.status(HttpStatus.OK).body(loginResponse);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/resend")
    public ResponseEntity<?> resendVerificationCode(@RequestParam String email) {
        try {
            authenticationService.resendVerificationCode(email);
            return ResponseEntity.ok("Verification code sent");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        try{
            authenticationService.sendPasswordResetEmail(email);
            return ResponseEntity.ok("Password reset email has been sent if the provided email is associated with an account.");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordDto resetPasswordDto) {
        try{
            LoginResponse loginResponse = authenticationService.resetPassword(resetPasswordDto);
            return ResponseEntity.ok().body(loginResponse);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
}
