package org.example.gobooking.service;

import org.example.gobooking.entity.company.Company;
import org.example.gobooking.entity.subscription.Subscription;
import org.example.gobooking.entity.user.Card;

public interface ValidSubscriptionService {

    void save(Company company, Subscription subscription, Card card);
}
