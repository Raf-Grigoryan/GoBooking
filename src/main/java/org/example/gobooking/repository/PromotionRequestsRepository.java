package org.example.gobooking.repository;

import org.example.gobooking.entity.request.PromotionRequest;
import org.example.gobooking.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromotionRequestsRepository extends JpaRepository<PromotionRequest, Integer> {

    PromotionRequest findPromotionRequestByRequesterId(int requesterId);

    void deletePromotionRequestByRequesterId(int requesterId);

    Page<PromotionRequest> findAllByRequesterEmailContaining(String email, Pageable pageable);

    int countPromotionRequestsByRequester(User user);

}
