package org.example.gobookingcommon.service;

import org.example.gobookingcommon.entity.company.Company;

public interface ValidSubscriptionService {

    void save(org.example.gobookingcommon.entity.company.Company company, String subscriptionTitle, String cardNumber);

    void deleteExpiredSubscriptions();
}
