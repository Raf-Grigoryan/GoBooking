package org.example.gobooking.service;

public interface MailService {

    void sendMailForUserVerify(String to, String token);
}
