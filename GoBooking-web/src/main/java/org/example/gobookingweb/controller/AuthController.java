package org.example.gobookingweb.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.gobookingcommon.dto.auth.PasswordChangeRequest;
import org.example.gobookingcommon.dto.auth.UserEditRequest;
import org.example.gobookingcommon.dto.card.SaveCardRequest;
import org.example.gobookingcommon.dto.user.SaveUserRequest;
import org.example.gobookingcommon.entity.user.User;
import org.example.gobookingcommon.service.BookingService;
import org.example.gobookingcommon.service.CardService;
import org.example.gobookingcommon.service.UserService;
import org.example.gobookingweb.security.CurrentUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    private final UserService userService;

    private final CardService cardService;

    private final PasswordEncoder passwordEncoder;

    private final BookingService bookingService;


    @GetMapping
    public String userPanel(@AuthenticationPrincipal CurrentUser user, ModelMap modelMap) {
        modelMap.addAttribute("cards", cardService.getCardsByUserId(user.getUser().getId()));
        modelMap.addAttribute("bookings", bookingService.clientFinishedBookings(user.getUser().getId()));
        modelMap.addAttribute("roleChangeRequestCount", userService.grtRoleChangeRequestCount(user.getUser()));
        return "/auth/user-main";
    }

    @GetMapping("/login")
    public String getLoginPage(@AuthenticationPrincipal CurrentUser currentUser) {
        if (currentUser == null) {
            log.info("User not logged in, redirecting to login page.");
            return "/auth/login";
        }
        log.info("User is already logged in, redirecting to the home page.");
        return "redirect:/";
    }

    @GetMapping("/register")
    public String getRegisterPage(@AuthenticationPrincipal CurrentUser currentUser) {
        if (currentUser == null) {
            return "/auth/register";
        }
        return "redirect:/loginSuccess";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute SaveUserRequest user) {
        log.info("Registering new user: {}", user.getName());
        userService.register(user);
        log.debug("User registration successful: {}", user.getName());
        return "redirect:/user/login";
    }

    @PostMapping("/change-password")
    public String changePasswordPage(@AuthenticationPrincipal CurrentUser user,
                                     @ModelAttribute PasswordChangeRequest passwordChangeRequest) {
        userService.changePassword(user.getUser(), passwordChangeRequest);
        log.debug("Password changed successfully for user: {}", user.getUser().getName());
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
        log.info("User is on the edit profile page.");
        return "/auth/edit-profile";
    }

    @PostMapping("/edit-profile")
    public String editProfile(@AuthenticationPrincipal CurrentUser user,
                              @Valid @ModelAttribute UserEditRequest userEditRequest,
                              @RequestParam("image") MultipartFile image) {
        boolean isEmailChanged = userService.editUser(user.getUser(), userEditRequest, image);
        if (isEmailChanged) {
            return "redirect:/loginSuccess";
        }
        return "redirect:/logout";

    }

    @GetMapping("/delete-profile")//G
    public String deleteProfile() {
        return "/auth/delete-profile";
    }

    @PostMapping("/delete-profile")
    public String deleteProfile(@AuthenticationPrincipal CurrentUser currentUser,
                                @RequestParam("password") String password,
                                @RequestParam("confirmPassword") String confirmPassword) {
        User user = currentUser.getUser();
        log.info("User {} is requesting to delete their profile.", user.getName());
        if (password.equals(confirmPassword) && passwordEncoder.matches(password, user.getPassword())) {
            userService.delete(user);
            log.debug("User profile deleted successfully for: {}", user.getName());
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
