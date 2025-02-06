package org.example.gobooking.mapper;

import org.example.gobooking.dto.card.CardResponse;
import org.example.gobooking.dto.card.SaveCardRequest;
import org.example.gobooking.entity.user.Card;
import org.example.gobooking.service.UserService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = {UserService.class},
        imports = {UserService.class}
)
public interface CardMapper {

    @Mapping(source = "userId",target = "user")
    Card toEntity(SaveCardRequest request);

    List<CardResponse> toDto(List<Card> cards);

    CardResponse toDto(Card card);
}