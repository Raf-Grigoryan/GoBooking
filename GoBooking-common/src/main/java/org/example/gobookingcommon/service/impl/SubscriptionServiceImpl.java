package org.example.gobookingcommon.service.impl;

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
        return subscriptionMapper.toDto(subscriptionRepository.findSubscriptionByTitle(title));
    }

    @Override
    public Subscription getSubscriptionByTitle(String title) {
        return subscriptionRepository.findSubscriptionByTitle(title);
    }
}
