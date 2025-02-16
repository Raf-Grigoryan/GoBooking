package org.example.gobooking.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.gobooking.customException.InsufficientFundsException;
import org.example.gobooking.entity.company.Company;
import org.example.gobooking.entity.subscription.Subscription;
import org.example.gobooking.entity.subscription.ValidSubscription;
import org.example.gobooking.entity.user.Card;
import org.example.gobooking.repository.CardRepository;
import org.example.gobooking.repository.ValidSubscriptionRepository;
import org.example.gobooking.service.ValidSubscriptionService;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;


@Service
@RequiredArgsConstructor
public class ValidSubscriptionServiceImpl implements ValidSubscriptionService {

    private final ValidSubscriptionRepository validSubscriptionRepository;
    private final CardRepository cardRepository;

    public void save(Company company, Subscription subscription, Card card) {
        if (card.getBalance().compareTo(subscription.getPrice()) < 0) {
            throw new InsufficientFundsException("Not enough balance on the card.");
        }
        card.setBalance(card.getBalance().subtract(subscription.getPrice()));
        ValidSubscription validSubscription = new ValidSubscription();
        validSubscription.setStartedDate(new Date());
        validSubscription.setCompany(company);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(validSubscription.getStartedDate());
        calendar.add(Calendar.MONTH, subscription.getDuration());
        Date endedDate = calendar.getTime();
        validSubscription.setEndedDate(endedDate);
        cardRepository.save(card);
        validSubscriptionRepository.save(validSubscription);
    }
}
