package org.example.gobooking.mapper;

import org.example.gobooking.dto.subscription.SaveSubscriptionRequest;
import org.example.gobooking.dto.subscription.SubscriptionDto;
import org.example.gobooking.entity.subscription.Subscription;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {

    Subscription toEntity(SaveSubscriptionRequest request);

    SubscriptionDto toDto(Subscription subscription);

    List<SubscriptionDto> toDtoList(List<Subscription> subscriptions);
}