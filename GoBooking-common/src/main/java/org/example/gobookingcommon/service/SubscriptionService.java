package org.example.gobookingcommon.service;


import org.example.gobookingcommon.dto.subscription.SubscriptionDto;
import org.example.gobookingcommon.entity.subscription.Subscription;

import java.util.List;

public interface SubscriptionService {

    void save (org.example.gobookingcommon.dto.subscription.SaveSubscriptionRequest saveSubscriptionRequest);

    List<SubscriptionDto> getAllSubscriptions();

    Subscription getSubscriptionByTitle(String title);

    SubscriptionDto getSubscriptionDtoByTitle(String title);
}
