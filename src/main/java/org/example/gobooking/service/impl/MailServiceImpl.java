package org.example.gobooking.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.gobooking.service.MailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailServiceImpl implements MailService {

    private final MailSender mailSender;


    @Value("${site.url}")
    private String url;

    @Override
    @Async
    public void sendMailForUserVerify(String to, String token) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Welcome to GoBooking Web Site! Please verify your account by clicking on this link ");
            message.setText(url + "/auth/verify?email=" + to + "&token=" + token);

            mailSender.send(message);
            log.info("Verification email sent to {}", to);
        } catch (Exception e) {
            log.error("Error sending verification email to {}: {}", to, e.getMessage());
        }
    }

    @Override
    @Async
    public void sendMailForPromotionRequest(List<String> adminsEmails, String requesterEmail, String context) {
        for (String adminEmail : adminsEmails) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(adminEmail);
            message.setSubject("Promotion Request from " + requesterEmail);
            message.setText("Hello, User" + requesterEmail + " has requested a promotion to Director" + " + context + "
                    + context);
            mailSender.send(message);
        }
    }

    @Async
    @Override
    public void sendMailForPromotionRequestAgree(String to) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Promotion Request Agree");
        message.setText("Hello " + to + " your promotion request agree");
        mailSender.send(message);
    }

    @Async
    @Override
    public void sendMailForPromotionRequestDisagree(String to) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Promotion Request Agree");
        message.setText("Hello " + to + " your promotion request disagree");
        mailSender.send(message);
    }

    @Async
    @Override
    public void sendMailForChangePassword(String to, String newPassword) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Password changed");
        message.setText("New password is " + newPassword + " " + to);
        mailSender.send(message);
    }

    @Override
    @Async
    public void sendMailForDeleteCard(String to, String context) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Your Card Deleted");
        message.setText("Your Card in " + context + " deleted");
        mailSender.send(message);
    }
}
