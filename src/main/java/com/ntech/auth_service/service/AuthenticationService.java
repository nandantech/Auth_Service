package com.ntech.auth_service.service;

import com.ntech.auth_service.dto.RegisterUserDto;
import com.ntech.auth_service.model.User;
import com.ntech.auth_service.repository.UserRepository;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class AuthenticationService {

    private UserRepository userRepository;
    private EmailService emailService;

    public AuthenticationService(UserRepository userRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    public User registerUser(RegisterUserDto registerUserDto) throws MessagingException {
        Optional<User> userOptional = userRepository.findByEmail(registerUserDto.getEmail());
        if (userOptional.isPresent() && userOptional.get().isEnabled()) {
            throw new IllegalArgumentException("A user with this email already exists.");
        }
        try {
            User user = new User(registerUserDto.getUsername(), registerUserDto.getEmail(), registerUserDto.getPassword());
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

}
