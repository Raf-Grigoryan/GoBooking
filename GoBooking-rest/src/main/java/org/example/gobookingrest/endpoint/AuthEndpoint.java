package org.example.gobookingrest.endpoint;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.gobookingcommon.dto.auth.PasswordChangeRequest;
import org.example.gobookingcommon.dto.auth.UserAuthRequest;
import org.example.gobookingcommon.dto.auth.UserAuthResponse;
import org.example.gobookingcommon.dto.auth.UserEditRequest;
import org.example.gobookingcommon.dto.booking.WorkerBookingResponse;
import org.example.gobookingcommon.dto.card.CardResponse;
import org.example.gobookingcommon.dto.user.PasswordConfirmationDto;
import org.example.gobookingcommon.dto.card.SaveCardRequestRest;
import org.example.gobookingcommon.dto.user.SaveUserRequest;
import org.example.gobookingcommon.service.BookingService;
import org.example.gobookingcommon.service.CardService;
import org.example.gobookingcommon.service.UserService;
import org.example.gobookingrest.security.CurrentUser;
import org.example.gobookingrest.util.JwtTokenUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Slf4j
public class AuthEndpoint {

    private final UserService userService;

    private final JwtTokenUtil jwtTokenUtil;

    private final BookingService bookingService;

    private final CardService cardService;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid UserAuthRequest userAuthRequest) {
        UserAuthResponse authResponse = userService.login(userAuthRequest);
        authResponse.setToken(jwtTokenUtil.generateToken(authResponse.getEmail()));
        log.info("User logged in: {}", authResponse.getEmail());
        return ResponseEntity.ok(authResponse);
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid SaveUserRequest saveUserRequest) {
        userService.register(saveUserRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("User successfully registered");
    }

    @PutMapping("/edit-profile")
    public ResponseEntity<?> editProfile(@AuthenticationPrincipal CurrentUser currentUser,
                                         @RequestBody @Valid UserEditRequest userEditRequest,
                                         @RequestParam("image") MultipartFile image) {
        boolean isEmailChanged = userService.editUser(currentUser.getUser(), userEditRequest, image);
        if (isEmailChanged) {
            return ResponseEntity.status(HttpStatus.CREATED).body("User edited successfully. Please verify your email");
        }

        return ResponseEntity.status(HttpStatus.OK).body("User edited successfully");
    }


    @GetMapping("/verify")
    public ResponseEntity<String> verify(@RequestParam String email,
                                         @RequestParam String token) {
        userService.verifyUserAccount(email, token);
        return ResponseEntity.ok("User verified");
    }

    @PostMapping("/create-card")
    public ResponseEntity<String> createCard(@Valid @RequestBody SaveCardRequestRest cardRequest) {
        cardService.save(cardRequest);
        return ResponseEntity.ok("Card created successfully");
    }


    @DeleteMapping("/delete-card")
    public ResponseEntity<?> deleteCard(@AuthenticationPrincipal CurrentUser user,
                                        @RequestParam("cardNumber") String cardNumber) {
        cardService.deleteCardByCardNumber(user.getUser().getEmail(), cardNumber);
        return ResponseEntity.ok("Card deleted successfully");
    }


    @GetMapping("/get-my-cards")
    public ResponseEntity<List<CardResponse>> myCards(@AuthenticationPrincipal CurrentUser user) {
        return ResponseEntity.ok(cardService.getCardsByUserId(user.getUser().getId()));
    }


    @GetMapping("/get-my-finished-bookings")
    public ResponseEntity<List<WorkerBookingResponse>> getMyBookings(@AuthenticationPrincipal CurrentUser user) {
        return ResponseEntity.ok(bookingService.clientFinishedBookings(user.getUser().getId()));
    }


    @PatchMapping("/change-password")
    public ResponseEntity<String> changePassword(@AuthenticationPrincipal CurrentUser user,
                                                 @RequestBody PasswordChangeRequest passwordChangeRequest) {
        userService.changePassword(user.getUser(), passwordChangeRequest);
        return ResponseEntity.ok("Password changed successfully");
    }

    @DeleteMapping("/delete-profile")
    public ResponseEntity<?> deleteProfile(@AuthenticationPrincipal CurrentUser currentUser,
                                @RequestBody PasswordConfirmationDto passwordConfirmationDto) {
        userService.delete(currentUser.getUser(), passwordConfirmationDto.getPassword(), passwordConfirmationDto.getConfirmPassword());
        return ResponseEntity.ok().build();
    }

}