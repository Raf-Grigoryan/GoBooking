package org.example.gobookingcommon.mapper;


import org.example.gobookingcommon.dto.request.PromotionRequestDto;
import org.example.gobookingcommon.entity.request.PromotionRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PromotionRequestMapper {

    PromotionRequestDto toDto(PromotionRequest promotionRequest);

}
