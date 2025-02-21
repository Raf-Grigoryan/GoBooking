package org.example.gobooking.service;

import org.example.gobooking.entity.company.Company;

public interface ValidSubscriptionService {

    void save(Company company, String subscriptionTitle, String cardNumber);

    void deleteExpiredSubscriptions();
}
