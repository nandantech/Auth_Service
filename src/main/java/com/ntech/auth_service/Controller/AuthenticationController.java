package com.ntech.auth_service.Controller;

import com.ntech.auth_service.dto.RegisterUserDto;
import com.ntech.auth_service.model.User;
import com.ntech.auth_service.service.AuthenticationService;
import com.ntech.auth_service.service.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {

    private JwtService jwtService;
    private AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
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
}
