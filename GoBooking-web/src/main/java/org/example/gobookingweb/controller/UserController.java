package org.example.gobookingweb.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.gobookingcommon.dto.request.RoleChangeRequestDto;
import org.example.gobookingcommon.dto.request.SavePromotionRequest;
import org.example.gobookingcommon.service.PromotionRequestsService;
import org.example.gobookingcommon.service.RoleChangeRequestService;
import org.example.gobookingweb.security.CurrentUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {


    private final PromotionRequestsService promotionRequestsService;

    private final RoleChangeRequestService roleChangeRequestService;


    @GetMapping("/send-promotion-request")
    public String promotionRequestsPage() {
        return "promotion_requests/promotion_requests";
    }

    @PostMapping("/send-promotion-request")
    public String sendPromotionRequests(@ModelAttribute @Valid SavePromotionRequest savePromotionRequest) {
        promotionRequestsService.savePromotion(savePromotionRequest);
        log.info("Promotion sent");
        return "promotion_requests/promotion_requests";
    }

    @GetMapping("/role-change-acceptance")
    public String roleChangeAcceptance(@AuthenticationPrincipal CurrentUser currentUser, ModelMap modelMap,
                                       @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                       @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        Page<RoleChangeRequestDto> roleChangeRequestDtoPage = roleChangeRequestService.findByEmployee(currentUser.getUser(), pageRequest);

        int totalPages = roleChangeRequestDtoPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .toList();
            modelMap.addAttribute("pageNumbers", pageNumbers);
        }
        modelMap.addAttribute("roleChangeRequestDtoPage", roleChangeRequestDtoPage);
        return "/user/role_change_acceptance";
    }

    @PostMapping("/agree")
    public String agree(@AuthenticationPrincipal CurrentUser currentUser,
                        @RequestParam("companyId") int companyId,
                        @RequestParam("agree") boolean agree) {
        roleChangeRequestService.agree(companyId, agree, currentUser.getUser());
        log.info("Role change request for companyId: {} processed successfully for user {}", companyId, currentUser.getUser().getName());
        return "redirect:/logout";
    }

}
