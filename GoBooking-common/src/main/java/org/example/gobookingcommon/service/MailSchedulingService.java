package org.example.gobookingcommon.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailSchedulingService {

    private final MailService mailService;
    private final ValidSubscriptionService validSubscriptionService;

    @Scheduled(cron = "0 0 1 * * ?")
    public void sendScheduledEmail() {
        mailService.sendScheduledEmail();
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void deleteExpiredSubscriptions() {
        validSubscriptionService.deleteExpiredSubscriptions();
    }
}
