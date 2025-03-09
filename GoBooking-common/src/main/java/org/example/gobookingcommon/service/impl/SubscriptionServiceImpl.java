package org.example.gobookingcommon.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.gobookingcommon.customException.SubscriptionOnlyExistException;
import org.example.gobookingcommon.dto.subscription.SaveSubscriptionRequest;
import org.example.gobookingcommon.dto.subscription.SubscriptionDto;
import org.example.gobookingcommon.entity.subscription.Subscription;
import org.example.gobookingcommon.mapper.SubscriptionMapper;
import org.example.gobookingcommon.repository.SubscriptionRepository;
import org.example.gobookingcommon.service.SubscriptionService;
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
        Subscription subscription = subscriptionRepository.findSubscriptionByTitle(title);
        if (subscription == null) {
            throw new EntityNotFoundException("Subscription not found");
        }
        return subscriptionMapper.toDto(subscription);
    }

    @Override
    public Subscription getSubscriptionByTitle(String title) {
        if (title == null) {
            throw new IllegalArgumentException("Subscription title cannot be null");
        }
        if (title.isEmpty()) {
            throw new IllegalArgumentException("Subscription title cannot be empty");
        }
        Subscription subscription = subscriptionRepository.findSubscriptionByTitle(title);
        if (subscription == null) {
            throw new EntityNotFoundException("Subscription not found");
        }
        return subscription;
    }
}
