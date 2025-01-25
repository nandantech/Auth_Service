package com.ntech.auth_service.service;

import com.ntech.auth_service.model.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {
    @Autowired
    private JavaMailSender emailSender;
    @Value("${spring.mail.password.reset.url}")
    private StringBuilder resetLink;

    public void sendPasswordResetEmail(User user) throws MessagingException {
        resetLink.append(user.getResetToken());
        sendEmail(user.getEmail(),"Password Reset", resetLink.toString());
    }

    public void sendVerificationEmail(User user) throws MessagingException { //TODO: Update with company logo
        String subject = "Account Verification";
        String verificationCode = "VERIFICATION CODE: " + user.getVerificationCode();
        String htmlMessage = "<html>"
                + "<body style=\"font-family: 'Roboto', sans-serif; margin: 0; padding: 0; background-color: #f7f7f7;\">"
                + "<div style=\"max-width: 600px; margin: 0 auto; padding: 20px; background-color: #ffffff; border-radius: 8px; box-shadow: 0 4px 10px rgba(0,0,0,0.1);\">"

                // Logo Section
                + "<div style=\"text-align: center; padding: 5px 0;\">"
                + "<img src=\"https://img1.wsimg.com/isteam/ip/c5aece07-21c0-4077-a9b8-59f636a0b78a/mylogo.jpg/:/rs=w:317,h:103,cg:true,m/cr=w:317,h:103/qt=q:100/ll\" alt=\"Ryt Mart Logo\" style=\"max-width: 150px;\">"
                + "</div>"

                // Welcome Message
                + "<div style=\"text-align: center; padding: 25px 0;\">"
                + "<h2 style=\"color: #333; font-size: 24px; font-weight: 600;\">Welcome to Ryt Mart!</h2>"
                + "<p style=\"font-size: 16px; color: #777;\">We're excited to have you on board. Please use the verification code below to complete your registration.</p>"
                + "</div>"

                // Verification Code Section
                + "<div style=\"background-color: #f8f9fa; padding: 20px; border-radius: 8px; text-align: center; margin-top: 20px;\">"
                + "<p style=\"font-size: 24px; font-weight: bold; color: #007bff; letter-spacing: 2px; margin: 0;\">"
                + verificationCode
                + "</p>"
                + "</div>"

                // Expiry Note Section
                + "<div style=\"text-align: center; padding: 20px 0; margin-top: 30px; display: block;\">"
                + "<p style=\"font-size: 14px; color: #777; margin: 0;\">If you did not request this, please ignore this email. Your code will expire in 15 minutes.</p>"
                + "</div>"

                // Footer Section
                + "<div style=\"text-align: center; padding-top: 30px; border-top: 1px solid #e0e0e0; display: block;\">"
                + "<p style=\"font-size: 12px; color: #aaa; margin: 0;\">&copy; 2025 Ryt Mart. All rights reserved.</p>"
                + "</div>"

                + "</div>"
                + "</body>"
                + "</html>";


        sendEmail(user.getEmail(), subject, htmlMessage);

    }

    public void sendEmail(String to, String subject, String text) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text, true);
        try {
            emailSender.send(message);
        }catch(Exception e){
            log.error(e.getMessage());
            throw new MessagingException("Unable to send the email. Please try again after some time");
        }
    }
}
