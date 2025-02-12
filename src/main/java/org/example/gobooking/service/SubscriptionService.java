package org.example.gobooking.service;

import org.example.gobooking.dto.subscription.SaveSubscriptionRequest;
import org.example.gobooking.dto.subscription.SubscriptionDto;
import org.example.gobooking.entity.subscription.Subscription;

import java.util.List;

public interface SubscriptionService {

    void save (SaveSubscriptionRequest saveSubscriptionRequest);

    List<SubscriptionDto> getAllSubscriptions();

    Subscription getSubscriptionByTitle(String title);

    SubscriptionDto getSubscriptionDtoByTitle(String title);
}
