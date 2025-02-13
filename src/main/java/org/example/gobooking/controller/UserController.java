package org.example.gobooking.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.gobooking.dto.request.RoleChangeRequestDto;
import org.example.gobooking.dto.request.SavePromotionRequest;
import org.example.gobooking.dto.user.SaveUserRequest;
import org.example.gobooking.security.CurrentUser;
import org.example.gobooking.service.CardService;
import org.example.gobooking.service.PromotionRequestsService;
import org.example.gobooking.service.RoleChangeRequestService;
import org.example.gobooking.service.UserService;
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
public class UserController {

    private final UserService userService;

    private final CardService cardService;

    private final PromotionRequestsService promotionRequestsService;

    private final RoleChangeRequestService roleChangeRequestService;


    @GetMapping
    public String userTest(@AuthenticationPrincipal CurrentUser user, ModelMap modelMap) {
        modelMap.addAttribute("cards", cardService.getCardsByUserId(user.getUser().getId()));
        modelMap.addAttribute("roleChangeRequestCount", roleChangeRequestService.countByEmployee(user.getUser()));
        return "/user/user-panel";
    }


    @GetMapping("/register")
    public String getRegisterPage(@AuthenticationPrincipal CurrentUser currentUser) {
        if (currentUser == null) {
            return "/user/register";
        }
        return "redirect:/";

    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute SaveUserRequest user) {
        userService.register(user);
        return "redirect:/user/login";
    }


    @GetMapping("/send-promotion-request")
    public String PromotionRequestsPage() {
        return "promotion_requests/promotion_requests";
    }

    @PostMapping("/send-promotion-request")
    public String PromotionRequests(@ModelAttribute @Valid SavePromotionRequest savePromotionRequest) {
        promotionRequestsService.savePromotion(savePromotionRequest);
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
                        @RequestParam("agree") boolean agree){
        roleChangeRequestService.agree(companyId, agree, currentUser.getUser());
        return "/user/user-panel";
    }

}
