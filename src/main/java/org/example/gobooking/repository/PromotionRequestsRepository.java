package org.example.gobooking.repository;

import org.example.gobooking.entity.request.PromotionRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromotionRequestsRepository extends JpaRepository<PromotionRequest, Integer> {

    PromotionRequest findPromotionRequestByRequesterId(int requesterId);

}
