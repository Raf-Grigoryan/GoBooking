package org.example.gobooking.mapper;

import org.example.gobooking.dto.request.PromotionRequestDto;
import org.example.gobooking.entity.request.PromotionRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PromotionRequestMapper {

    PromotionRequestDto toDto(PromotionRequest promotionRequest);

}
