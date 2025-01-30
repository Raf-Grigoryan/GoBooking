package org.example.gobooking.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.gobooking.dto.request.SavePromotionRequest;
import org.example.gobooking.dto.user.SaveUserRequest;
import org.example.gobooking.service.PromotionRequestsService;
import org.example.gobooking.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final PromotionRequestsService promotionRequestsService;


    @GetMapping("/login")
    public String getLoginPage() {
        return "/user/login";
    }

    @GetMapping("/register")
    public String getRegisterPage() {
        return "/user/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute SaveUserRequest user) {
        userService.register(user);
        return "redirect:/user/login";
    }

    @GetMapping("/verify")
    public String getVerifyPage(@RequestParam("email") String email,
                                @RequestParam("token") String token) {
        userService.verifyUserAccount(email, token);
        return "/user/login";
    }

    @GetMapping("/send-promotion-request")
    public String PromotionRequestsPage() {return "promotion_requests/promotion_requests";}

    @PostMapping("/send-promotion-request")
    public String PromotionRequests(@ModelAttribute @Valid SavePromotionRequest savePromotionRequest) {
        promotionRequestsService.savePromotion(savePromotionRequest);
        return "promotion_requests/promotion_requests";
    }

}
