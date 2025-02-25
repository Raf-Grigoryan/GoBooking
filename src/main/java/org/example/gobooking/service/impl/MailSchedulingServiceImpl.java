package org.example.gobooking.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.gobooking.service.MailSchedulingService;
import org.example.gobooking.service.MailService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailSchedulingServiceImpl implements MailSchedulingService {

    private final MailService mailService;

    @Scheduled(cron = "0 0 1 * * ?")
    @Override
    public void sendScheduledEmail() {
        mailService.sendScheduledEmail();
    }
}
