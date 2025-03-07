package org.example.gobookingcommon.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.example.gobookingcommon.customException.CardNotExistException;
import org.example.gobookingcommon.customException.InsufficientFundsException;
import org.example.gobookingcommon.entity.company.Company;
import org.example.gobookingcommon.entity.subscription.Subscription;
import org.example.gobookingcommon.entity.subscription.ValidSubscription;
import org.example.gobookingcommon.entity.user.Card;
import org.example.gobookingcommon.service.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ValidSubscriptionServiceImpl implements ValidSubscriptionService {

    private final org.example.gobookingcommon.repository.ValidSubscriptionRepository validSubscriptionRepository;
    private final CardService cardService;
    private final SubscriptionService subscriptionService;
    private final MailService mailService;
    private final ProjectFinanceService projectFinanceService;

    @Transactional
    public void save(Company company, String subscriptionTitle, String cardNumber) {
        Subscription subscription = subscriptionService.getSubscriptionByTitle(subscriptionTitle);
        Card card = cardService.getCardByCardNumber(cardNumber);
        if (card == null){
            throw new CardNotExistException("Card not exist");
        }
        if (card.getBalance().compareTo(subscription.getPrice()) < 0) {
            throw new InsufficientFundsException("Not enough balance on the card.");
        }
        card.setBalance(card.getBalance().subtract(subscription.getPrice()));
        projectFinanceService.addFunds(subscription.getPrice());
        ValidSubscription validSubscription = new ValidSubscription();
        validSubscription.setStartedDate(new Date());
        validSubscription.setCompany(company);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(validSubscription.getStartedDate());
        calendar.add(Calendar.MONTH, subscription.getDuration());
        Date endedDate = calendar.getTime();
        validSubscription.setEndedDate(endedDate);
        cardService.save(card);
        validSubscriptionRepository.save(validSubscription);
    }

    @Scheduled(cron = "0 0 9 * * ?")
    public void notifyExpiringSubscriptions() {
        Date notifyDate = new Date(System.currentTimeMillis() + (3L * 24 * 60 * 60 * 1000)); // Current time + 3 days
        List<ValidSubscription> expiringSoon = validSubscriptionRepository.findByEndedDate(new java.sql.Date(notifyDate.getTime()));

        for (ValidSubscription sub : expiringSoon) {
            mailService.sendSubscriptionExpiryEmail(sub.getCompany().getDirector().getEmail(), sub.getCompany().getDirector().getName(), sub.getEndedDate().toString());
        }
    }


    @Override
    public void deleteExpiredSubscriptions() {
        Date today = new Date();
        List<ValidSubscription> expiredSubscriptions = validSubscriptionRepository.findByEndedDateBefore(today);
        if (!expiredSubscriptions.isEmpty()) {
            for (ValidSubscription sub : expiredSubscriptions) {
                mailService.sendSubscriptionDeletedEmail(sub.getCompany().getDirector().getEmail(), sub.getCompany().getDirector().getName());
            }

            validSubscriptionRepository.deleteAll(expiredSubscriptions);
            System.out.println("Deleted " + expiredSubscriptions.size() + " expired subscriptions.");
        }
    }
}
