package com.ntech.auth_service.controller;

import com.ntech.auth_service.config.responses.LoginResponse;
import com.ntech.auth_service.dto.RegisterUserDto;
import com.ntech.auth_service.dto.VerifyUserDto;
import com.ntech.auth_service.model.User;
import com.ntech.auth_service.service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/verify")
    public  ResponseEntity<?> verifyUser(@Valid @RequestBody VerifyUserDto verifyUserDto) {
        try{
             LoginResponse loginResponse = authenticationService.verifyUser(verifyUserDto);
             if(loginResponse.isSuccess()){
                 return ResponseEntity.status(HttpStatus.OK).body(loginResponse);
             }else{
                 return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(loginResponse);
             }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
