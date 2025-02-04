package org.example.gobooking.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.gobooking.customException.AlreadyDirectorRequestedException;
import org.example.gobooking.dto.request.PromotionRequestDto;
import org.example.gobooking.dto.request.SavePromotionRequest;
import org.example.gobooking.entity.request.PromotionRequest;
import org.example.gobooking.entity.user.Role;
import org.example.gobooking.entity.user.User;
import org.example.gobooking.mapper.PromotionRequestMapper;
import org.example.gobooking.repository.PromotionRequestsRepository;
import org.example.gobooking.service.MailService;
import org.example.gobooking.service.PromotionRequestsService;
import org.example.gobooking.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PromotionRequestsServiceImpl implements PromotionRequestsService {

    private final PromotionRequestsRepository promotionRequestsRepository;

    private final MailService mailService;

    private final UserService userService;

    private final PromotionRequestMapper promotionRequestMapper;


    @Override
    public void savePromotion(SavePromotionRequest request) {
        if (promotionRequestsRepository.findPromotionRequestByRequesterId(request.getRequester_id()) == null) {
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


    @Override
    public Page<PromotionRequestDto> getAllPromotions(PageRequest pageRequest) {
        Page<PromotionRequest> promotionRequests = promotionRequestsRepository.findAll(pageRequest);
        return promotionRequests.map(promotionRequestMapper::toDto);
    }

    @Transactional
    @Override
    public void agree(int requesterId, boolean agree) {
        if (agree) {
            User user = userService.getUserById(requesterId);
            user.setRole(Role.DIRECTOR);
            userService.saveUser(user);
            mailService.sendMailForPromotionRequestAgree(user.getEmail());
            promotionRequestsRepository.deletePromotionRequestByRequesterId(requesterId);
        } else {
            mailService.sendMailForPromotionRequestDisagree(userService.getUserById(requesterId).getEmail());
            promotionRequestsRepository.deletePromotionRequestByRequesterId(requesterId);
        }

    }

    @Override
    public Page<PromotionRequestDto> getAllPromotionsByRequesterEmail(PageRequest pageRequest, String email) {
        Page<PromotionRequest> promotionRequests = promotionRequestsRepository.findAllByRequesterEmailContaining(email, pageRequest);
        return promotionRequests.map(promotionRequestMapper::toDto);
    }


}
