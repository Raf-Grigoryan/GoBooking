package org.example.gobookingweb.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.gobookingcommon.dto.request.RoleChangeRequestDto;
import org.example.gobookingcommon.dto.request.SavePromotionRequest;
import org.example.gobookingcommon.dto.user.SaveUserRequest;
import org.example.gobookingcommon.service.CardService;
import org.example.gobookingcommon.service.PromotionRequestsService;
import org.example.gobookingcommon.service.RoleChangeRequestService;
import org.example.gobookingcommon.service.UserService;
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

    private final UserService userService;

    private final CardService cardService;

    private final PromotionRequestsService promotionRequestsService;

    private final RoleChangeRequestService roleChangeRequestService;


    @GetMapping
    public String userTest(@AuthenticationPrincipal CurrentUser user, ModelMap modelMap) {
        log.info("User {} accessing user panel", user.getUser().getName());

        modelMap.addAttribute("cards", cardService.getCardsByUserId(user.getUser().getId()));
        modelMap.addAttribute("roleChangeRequestCount", roleChangeRequestService.countByEmployee(user.getUser()));

        return "/user/user-panel";
    }

    @GetMapping("/register")
    public String getRegisterPage(@AuthenticationPrincipal CurrentUser currentUser) {
        if (currentUser == null) {
            log.info("Navigating to registration page.");
            return "/user/register";
        }
        log.info("User {} already logged in. Redirecting to home page.", currentUser.getUser().getName());
        return "redirect:/";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute SaveUserRequest user) {
        log.info("Registering new user: {}", user.getName());
        userService.register(user);
        log.debug("User registration successful: {}", user.getName());
        return "redirect:/user/login";
    }

    @GetMapping("/send-promotion-request")
    public String promotionRequestsPage() {
        log.info("Navigating to send promotion request page.");
        return "promotion_requests/promotion_requests";
    }

    @PostMapping("/send-promotion-request")
    public String promotionRequests(@ModelAttribute @Valid SavePromotionRequest savePromotionRequest) {
        log.info("Sending promotion request for: {}", savePromotionRequest);
        promotionRequestsService.savePromotion(savePromotionRequest);
        log.debug("Promotion request successfully sent for: {}", savePromotionRequest);
        return "promotion_requests/promotion_requests";
    }

    @GetMapping("/role-change-acceptance")
    public String roleChangeAcceptance(@AuthenticationPrincipal CurrentUser currentUser, ModelMap modelMap,
                                       @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                       @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        log.info("User {} accessing role change acceptance page.", currentUser.getUser().getName());

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
        log.debug("Fetched {} role change requests for user {}.", roleChangeRequestDtoPage.getContent().size(), currentUser.getUser().getName());

        return "/user/role_change_acceptance";
    }

    @PostMapping("/agree")
    public String agree(@AuthenticationPrincipal CurrentUser currentUser,
                        @RequestParam("companyId") int companyId,
                        @RequestParam("agree") boolean agree) {
        log.info("User {} responding to role change request for companyId: {} with decision: {}",
                currentUser.getUser().getName(), companyId, agree ? "AGREE" : "DISAGREE");

        roleChangeRequestService.agree(companyId, agree, currentUser.getUser());
        log.debug("Role change request for companyId: {} processed successfully for user {}", companyId, currentUser.getUser().getName());

        return "redirect:/logout";
    }

}
