package org.example.gobookingcommon.service;


public interface ValidSubscriptionService {

    void save(org.example.gobookingcommon.entity.company.Company company, String subscriptionTitle, String cardNumber);

    void deleteExpiredSubscriptions();
}
