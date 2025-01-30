package org.example.gobooking.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.gobooking.customException.AlreadyDirectorRequestedException;
import org.example.gobooking.dto.request.SavePromotionRequest;
import org.example.gobooking.entity.request.PromotionRequest;
import org.example.gobooking.entity.user.User;
import org.example.gobooking.repository.PromotionRequestsRepository;
import org.example.gobooking.service.MailService;
import org.example.gobooking.service.PromotionRequestsService;
import org.example.gobooking.service.UserService;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PromotionRequestsServiceImpl implements PromotionRequestsService {

    private final PromotionRequestsRepository promotionRequestsRepository;

    private final MailService mailService;

    private final UserService userService;

    @Override
    public void savePromotion(SavePromotionRequest request) {
        if (promotionRequestsRepository.findPromotionRequestByRequester_id(request.getRequester_id()) == null) {
            List<String> adminEmails = userService.getAdminsEmails();
            User user = userService.getUserById(request.getRequester_id());
            mailService.sendMailForPromotionRequest(adminEmails, user.getEmail(), request.getContext());
            promotionRequestsRepository.save(PromotionRequest.builder()
                    .requester(user)
                    .message(request.getContext())
                    .build());
        } else {
            throw new AlreadyDirectorRequestedException("A request to become a director has already been submitted");
        }
    }
}
