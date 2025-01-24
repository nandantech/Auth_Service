package com.ntech.auth_service.service;

import com.ntech.auth_service.dto.LoginUserDto;
import com.ntech.auth_service.responses.LoginResponse;
import com.ntech.auth_service.dto.RegisterUserDto;
import com.ntech.auth_service.dto.VerifyUserDto;
import com.ntech.auth_service.model.User;
import com.ntech.auth_service.repository.UserRepository;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class AuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(
            UserRepository userRepository,
            EmailService emailService,
            JwtService jwtService,
            PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public User registerUser(RegisterUserDto registerUserDto) throws MessagingException {

        Optional<User> userEmailOptional = userRepository.findByEmail(registerUserDto.getEmail());
        Optional<User> usernameOptional = userRepository.findByUsername(registerUserDto.getUsername());

        if (userEmailOptional.isPresent() && userEmailOptional.get().isEnabled()) {
            throw new IllegalArgumentException("A user with this email already exists.");
        }

        if (usernameOptional.isPresent() && usernameOptional.get().isEnabled()) {
            throw new IllegalArgumentException("This username is already taken!");
        }

        try {
            User user = new User(registerUserDto.getUsername(), registerUserDto.getEmail(), passwordEncoder.encode(registerUserDto.getPassword()));
            deleteUser(user);
            user.setVerificationCode(generateVerificationCode());
            user.setVerificationExpiresAt(LocalDateTime.now().plusMinutes(15));
            user.setEnabled(false);
            emailService.sendVerificationEmail(user);
            return userRepository.save(user);
        } catch (MessagingException e) {
            throw new MessagingException(e.getMessage());
        }
    }

    public LoginResponse verifyUser(VerifyUserDto verifyUserDto){
        try{
            Optional<User> userOptional = userRepository.findByEmail(verifyUserDto.getEmail());

            if(userOptional.isPresent() && !userOptional.get().isEnabled()) {

                User user = userOptional.get();
                if(user.getVerificationCode()!=null && user.getVerificationExpiresAt().isBefore(LocalDateTime.now())){
                    return new LoginResponse("Verification code expired!");
                }

                if(user.getVerificationCode()!=null && user.getVerificationCode().equals(verifyUserDto.getVerificationCode())){
                    user.setEnabled(true);
                    user.setVerificationCode(null);
                    user.setVerificationExpiresAt(null);
                    userRepository.save(user);
                    return generateLoginResponse(user);
                }else{
                    return new LoginResponse("Invalid verification code!");
                }

            }else{
                return new LoginResponse("User Not Found or already verified!");
            }
        }catch (Exception e){
            logger.error("An error occurred while verifying the user: {}", e.getMessage(), e);
            return new LoginResponse(e.getMessage());
        }
    }

    public LoginResponse generateLoginResponse(User user){
        String jwtToken = jwtService.generateToken(user);
        return new LoginResponse(jwtToken, jwtService.getExpirationTime());
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(900000)+100000;
        return String.valueOf(code);
    }

    public void deleteUser(User inputUser) {
        Optional<User> userOptional = userRepository.findByEmail(inputUser.getEmail());
        if (userOptional.isPresent()) {
            Long id = userOptional.get().getId();
            userRepository.deleteById(id);
        }
    }

    public LoginResponse authenticate(LoginUserDto loginUserDto) {
        Optional<User> userOptional = userRepository.findByEmail(loginUserDto.getEmail());
        if (userOptional.isEmpty()) {
            return new LoginResponse("User Not Found");
        }
        User user = userOptional.get();
        if(!user.isEnabled()){
            return new LoginResponse("Account not verified. Please verify your account.");
        }
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUserDto.getEmail(),
                        loginUserDto.getPassword()
                )
        );
        return generateLoginResponse(user);
    }

    public void resendVerificationCode(String email) throws MessagingException {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.isEnabled()) {
                throw new RuntimeException("Account is already verified");
            }
            user.setVerificationCode(generateVerificationCode());
            user.setVerificationExpiresAt(LocalDateTime.now().plusHours(1));
            emailService.sendVerificationEmail(user);
            userRepository.save(user);
        } else {
            throw new RuntimeException("User not found");
        }
    }
}
