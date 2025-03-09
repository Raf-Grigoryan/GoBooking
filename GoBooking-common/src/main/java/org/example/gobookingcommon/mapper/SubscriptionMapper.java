package org.example.gobookingcommon.mapper;

import org.example.gobookingcommon.dto.subscription.SaveSubscriptionRequest;
import org.example.gobookingcommon.dto.subscription.SubscriptionDto;
import org.example.gobookingcommon.entity.subscription.Subscription;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {

    Subscription toEntity(SaveSubscriptionRequest request);

    SubscriptionDto toDto(Subscription subscription);

    List<SubscriptionDto> toDtoList(List<Subscription> subscriptions);
}