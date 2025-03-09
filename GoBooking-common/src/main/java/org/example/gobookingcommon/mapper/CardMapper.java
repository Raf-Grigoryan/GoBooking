package org.example.gobookingcommon.mapper;


import org.example.gobookingcommon.dto.card.CardResponse;
import org.example.gobookingcommon.dto.card.SaveCardRequest;
import org.example.gobookingcommon.dto.card.SaveCardRequestRest;
import org.example.gobookingcommon.entity.user.Card;
import org.example.gobookingcommon.service.UserService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = {org.example.gobookingcommon.service.UserService.class},
        imports = {UserService.class}
)
public interface CardMapper {

    @Mapping(source = "userId",target = "user")
    Card toEntity(SaveCardRequest request);

    @Mapping(source = "userId",target = "user")
    Card toEntity(SaveCardRequestRest request);

    List<CardResponse> toDto(List<Card> cards);

    CardResponse toDto(Card card);
}