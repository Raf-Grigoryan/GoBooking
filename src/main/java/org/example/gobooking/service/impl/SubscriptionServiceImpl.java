package org.example.gobooking.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.gobooking.customException.SubscriptionOnlyExistException;
import org.example.gobooking.dto.subscription.SaveSubscriptionRequest;
import org.example.gobooking.dto.subscription.SubscriptionDto;
import org.example.gobooking.entity.subscription.Subscription;
import org.example.gobooking.mapper.SubscriptionMapper;
import org.example.gobooking.repository.SubscriptionRepository;
import org.example.gobooking.service.SubscriptionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionMapper subscriptionMapper;

    @Override
    public void save(SaveSubscriptionRequest saveSubscriptionRequest) {
        if (subscriptionRepository.existsSubscriptionByTitle(saveSubscriptionRequest.getTitle())) {
            throw new SubscriptionOnlyExistException("Subscription only exist");
        }
        subscriptionRepository.save(subscriptionMapper.toEntity(saveSubscriptionRequest));
    }

    @Override
    public List<SubscriptionDto> getAllSubscriptions() {
        return subscriptionMapper.toDtoList(subscriptionRepository.findAll());
    }

    @Override
    public SubscriptionDto getSubscriptionDtoByTitle(String title) {
        return subscriptionMapper.toDto(subscriptionRepository.findSubscriptionByTitle(title));
    }

    @Override
    public Subscription getSubscriptionByTitle(String title) {
        return subscriptionRepository.findSubscriptionByTitle(title);
    }
}
