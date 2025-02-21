package org.example.gobooking.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.gobooking.dto.auth.PasswordChangeRequest;
import org.example.gobooking.dto.auth.UserEditRequest;
import org.example.gobooking.dto.card.SaveCardRequest;
import org.example.gobooking.entity.user.User;
import org.example.gobooking.security.CurrentUser;
import org.example.gobooking.service.CardService;
import org.example.gobooking.service.UserService;
import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final  UserService userService;

    private final CardService cardService;

    private final PasswordEncoder passwordEncoder;


    @GetMapping("/login")
    public String getLoginPage(@AuthenticationPrincipal CurrentUser currentUser) {
        if (currentUser == null) {
            return "/auth/login";
        }
        return "redirect:/";
    }


    @PostMapping("/change-password")
    public String changePasswordPage(@AuthenticationPrincipal CurrentUser user,
                                     @ModelAttribute PasswordChangeRequest passwordChangeRequest) {
        userService.changePassword(user.getUser(), passwordChangeRequest);
        return "/auth/login";
    }

    @GetMapping("/verify")
    public String getVerifyPage(@RequestParam("email") String email,
                                @RequestParam("token") String token) {
        userService.verifyUserAccount(email, token);
        return "/auth/login";
    }

    @GetMapping("/create-card")
    public String createCardPage() {
        return "/auth/card";
    }


    @PostMapping("/create-card")
    public String createCard(@Valid @ModelAttribute SaveCardRequest cardRequest) {
        cardService.save(cardRequest);
        return "redirect:/loginSuccess";
    }

    @GetMapping("/edit-profile")
    public String editProfilePage() {
        return "/auth/edit-profile";
    }

    @PostMapping("/edit-profile")
    public String editProfile(@AuthenticationPrincipal CurrentUser user,
                              @Valid @ModelAttribute UserEditRequest userEditRequest,
                              @RequestParam("image") MultipartFile image) {
        if (userService.editUser(user.getUser(), userEditRequest, image)) {
            return "redirect:/loginSuccess";
        }
        return "redirect:/logout";
    }

    @GetMapping("/delete-profile")
    public String deleteProfile(){
        return "/auth/delete-profile";
    }

    @PostMapping("/delete-profile")
    public String deleteProfile(@AuthenticationPrincipal CurrentUser currentUser,
                                @RequestParam("password") String password,
                                @RequestParam("confirmPassword") String confirmPassword) {
        User user = currentUser.getUser();
        if (password.equals(confirmPassword) && passwordEncoder.matches(password, user.getPassword())) {
            userService.delete(user);
            return "redirect:/logout";
        }
        return "redirect:/auth/delete-profile?error=true";
    }

    @GetMapping("/delete-card")
    public String deleteCard(@AuthenticationPrincipal CurrentUser currentUser, @RequestParam("cardNumber") String cardNumber) {
        cardService.deleteCardByCardNumber(currentUser.getUser().getEmail(), cardNumber);
        return "redirect:/loginSuccess";
    }

}
