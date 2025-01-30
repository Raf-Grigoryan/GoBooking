package org.example.gobooking.service.impl;

import lombok.RequiredArgsConstructor;

import org.example.gobooking.service.MailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final MailSender mailSender;

    @Value("${site.url}")
    private String url;

    @Override
    @Async
    public void sendMailForUserVerify(String to, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Welcome to GoBooking Web Site! Please verify your account by clicking on this link ");
        message.setText(url + "/user/verify?email=" + to + "&token=" + token);
        mailSender.send(message);
    }
}
