package org.example.gobooking.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.example.gobooking.dto.card.SaveCardRequest;
import org.example.gobooking.dto.user.SaveUserRequest;
import org.example.gobooking.service.CardService;
import org.example.gobooking.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final CardService cardService;


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

    @GetMapping("/create-card")
    public String createCardPage() {
        return "/card/card";
    }

    @PostMapping("/create-card")
    public String createCard(@Valid @ModelAttribute SaveCardRequest cardRequest) {
        cardService.save(cardRequest);
        return "redirect:/";
    }
}
