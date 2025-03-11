package org.example.gobookingcommon.service.impl;

import org.example.gobookingcommon.customException.AlreadyDirectorRequestedException;
import org.example.gobookingcommon.dto.request.PromotionRequestDto;
import org.example.gobookingcommon.dto.request.SavePromotionRequest;
import org.example.gobookingcommon.entity.request.PromotionRequest;
import org.example.gobookingcommon.entity.user.Role;
import org.example.gobookingcommon.entity.user.User;
import org.example.gobookingcommon.mapper.PromotionRequestMapper;
import org.example.gobookingcommon.repository.PromotionRequestsRepository;
import org.example.gobookingcommon.service.MailService;
import org.example.gobookingcommon.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PromotionRequestsServiceImplTest {

    @Mock
    private PromotionRequestsRepository promotionRequestsRepository;

    @Mock
    private MailService mailService;

    @Mock
    private UserService userService;

    @Mock
    private PromotionRequestMapper promotionRequestMapper;

    @InjectMocks
    private PromotionRequestsServiceImpl promotionRequestsService;

    private SavePromotionRequest request;
    private User user;

    @BeforeEach
    void setUp() {
        request = new SavePromotionRequest();
        request.setRequester_id(1);
        request.setContext("Requesting promotion");

        user = new User();
        user.setId(1);
        user.setEmail("user@example.com");
    }


    @Test
    void savePromotion_ExistingRequest_ThrowsException() {
        when(promotionRequestsRepository.findPromotionRequestByRequesterId(1)).thenReturn(new PromotionRequest());

        assertThrows(AlreadyDirectorRequestedException.class, () -> promotionRequestsService.savePromotion(request));

        verify(mailService, never()).sendMailForPromotionRequest(any(), any(), any());
        verify(promotionRequestsRepository, never()).save(any());
    }

    @Test
    void savePromotion_NewRequest_Success() {
        when(promotionRequestsRepository.findPromotionRequestByRequesterId(1)).thenReturn(null);
        when(userService.getAdminsEmails()).thenReturn(List.of("admin@example.com"));
        when(userService.getUserById(1)).thenReturn(user);

        promotionRequestsService.savePromotion(request);

        verify(mailService, times(1)).sendMailForPromotionRequest(any(), eq("user@example.com"), eq("Requesting promotion"));
        verify(promotionRequestsRepository, times(1)).save(any(PromotionRequest.class));
    }



    @Test
    void getAllPromotions_ReturnsPageOfDtos() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        PromotionRequest promotionRequest = new PromotionRequest();
        PromotionRequestDto promotionRequestDto = new PromotionRequestDto();
        Page<PromotionRequest> promotionRequestPage = new PageImpl<>(List.of(promotionRequest));

        when(promotionRequestsRepository.findAll(pageRequest)).thenReturn(promotionRequestPage);
        when(promotionRequestMapper.toDto(promotionRequest)).thenReturn(promotionRequestDto);

        Page<PromotionRequestDto> result = promotionRequestsService.getAllPromotions(pageRequest);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(promotionRequestsRepository, times(1)).findAll(pageRequest);
        verify(promotionRequestMapper, times(1)).toDto(promotionRequest);
    }


    @Test
    void agree_WhenAgreeTrue_UpdatesUserRoleAndSendsMail() {
        User user = new User();
        user.setId(1);
        user.setEmail("user@example.com");
        when(userService.getUserById(1)).thenReturn(user);

        promotionRequestsService.agree(1, true);

        verify(userService, times(1)).saveUser(user);
        verify(mailService, times(1)).sendMailForPromotionRequestAgree("user@example.com");
        verify(promotionRequestsRepository, times(1)).deletePromotionRequestByRequesterId(1);
        assertEquals(Role.DIRECTOR, user.getRole());
    }

    @Test
    void agree_WhenAgreeFalse_SendsDisagreeMail() {
        User user = new User();
        user.setId(1);
        user.setEmail("user@example.com");
        when(userService.getUserById(1)).thenReturn(user);

        promotionRequestsService.agree(1, false);

        verify(mailService, times(1)).sendMailForPromotionRequestDisagree("user@example.com");
        verify(promotionRequestsRepository, times(1)).deletePromotionRequestByRequesterId(1);
        verify(userService, never()).saveUser(any());
    }

}
