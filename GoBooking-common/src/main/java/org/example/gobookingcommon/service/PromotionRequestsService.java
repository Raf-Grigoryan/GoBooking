package org.example.gobookingcommon.service;


import org.example.gobookingcommon.dto.request.PromotionRequestDto;
import org.example.gobookingcommon.dto.request.SavePromotionRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface PromotionRequestsService {

    void savePromotion(SavePromotionRequest request);

    Page<PromotionRequestDto> getAllPromotions(PageRequest pageRequest);

    void agree(int requesterId, boolean agree);

    Page<PromotionRequestDto> getAllPromotionsByRequesterEmail(PageRequest pageRequest, String email);
}
