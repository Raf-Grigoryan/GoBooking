package org.example.gobooking.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.gobooking.dto.auth.PasswordChangeRequest;
import org.example.gobooking.dto.auth.UserEditRequest;
import org.example.gobooking.dto.card.SaveCardRequest;
import org.example.gobooking.security.CurrentUser;
import org.example.gobooking.service.CardService;
import org.example.gobooking.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    private final UserService userService;

    private final CardService cardService;

    @GetMapping("/login")
    public String getLoginPage(@AuthenticationPrincipal CurrentUser currentUser) {
        if (currentUser == null) {
            log.info("User not logged in, redirecting to login page.");
            return "/auth/login";
        }
        log.info("User is already logged in, redirecting to the home page.");
        return "redirect:/";
    }

    @PostMapping("/change-password")
    public String changePasswordPage(@AuthenticationPrincipal CurrentUser user,
                                     @ModelAttribute PasswordChangeRequest passwordChangeRequest) {
        log.info("User {} is changing their password.", user.getUser().getName());
        userService.changePassword(user.getUser(), passwordChangeRequest);
        log.debug("Password changed successfully for user: {}", user.getUser().getName());
        return "/auth/login";
    }

    @GetMapping("/verify")
    public String getVerifyPage(@RequestParam("email") String email,
                                @RequestParam("token") String token) {
        log.info("Verifying user account for email: {} with token: {}", email, token);
        userService.verifyUserAccount(email, token);
        log.debug("User account verified for email: {}", email);
        return "/auth/login";
    }

    @GetMapping("/create-card")
    public String createCardPage() {
        log.info("User is on the create card page.");
        return "/auth/card";
    }

    @PostMapping("/create-card")
    public String createCard(@Valid @ModelAttribute SaveCardRequest cardRequest) {
        log.info("Creating a new card with details: {}", cardRequest);
        cardService.save(cardRequest);
        log.debug("Card created successfully.");
        return "redirect:/loginSuccess";
    }

    @GetMapping("/edit-profile")
    public String editProfilePage() {
        log.info("User is on the edit profile page.");
        return "/auth/edit-profile";
    }

    @PostMapping("/edit-profile")
    public String editProfile(@AuthenticationPrincipal CurrentUser user,
                              @Valid @ModelAttribute UserEditRequest userEditRequest,
                              @RequestParam("image") MultipartFile image) {
        log.info("User {} is attempting to edit their profile.", user.getUser().getName());
        boolean isUpdated = userService.editUser(user.getUser(), userEditRequest, image);
        if (isUpdated) {
            log.debug("User profile updated successfully for: {}", user.getUser().getName());
            return "redirect:/loginSuccess";
        } else {
            log.warn("Failed to update user profile for: {}", user.getUser().getName());
            return "redirect:/logout";
        }
    }

    @GetMapping("/delete-profile")
    public String deleteProfile(@AuthenticationPrincipal CurrentUser user) {
        log.info("User {} is requesting to delete their profile.", user.getUser().getName());
        userService.delete(user.getUser());
        log.debug("User profile deleted successfully for: {}", user.getUser().getName());
        return "redirect:/logout";
    }

    @GetMapping("/delete-card")
    public String deleteCard(@AuthenticationPrincipal CurrentUser currentUser, @RequestParam("cardNumber") String cardNumber) {
        log.info("User {} is deleting card with number: {}", currentUser.getUser().getName(), cardNumber);
        cardService.deleteCardByCardNumber(currentUser.getUser().getEmail(), cardNumber);
        log.debug("Card with number {} deleted successfully for user: {}", cardNumber, currentUser.getUser().getName());
        return "redirect:/loginSuccess";
    }


}
