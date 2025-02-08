package org.example.gobooking.mapper;

import org.example.gobooking.dto.subscription.SaveSubscriptionRequest;
import org.example.gobooking.entity.subscription.Subscription;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {

    Subscription toEntity(SaveSubscriptionRequest request);
}