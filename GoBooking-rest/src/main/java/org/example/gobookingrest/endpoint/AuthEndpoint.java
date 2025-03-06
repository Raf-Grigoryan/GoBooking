package org.example.gobookingrest.endpoint;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.gobookingcommon.dto.auth.PasswordChangeRequest;
import org.example.gobookingcommon.dto.auth.UserAuthRequest;
import org.example.gobookingcommon.dto.auth.UserAuthResponse;
import org.example.gobookingcommon.dto.auth.UserEditRequest;
import org.example.gobookingcommon.dto.booking.WorkerBookingResponse;
import org.example.gobookingcommon.dto.card.CardResponse;
import org.example.gobookingcommon.dto.card.SaveCardRequestRest;
import org.example.gobookingcommon.dto.user.PasswordConfirmationDto;
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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully logged in and token generated"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid credentials or account not verified, Incorrect Password"),
            @ApiResponse(responseCode = "404", description = "Not Found - User does not exist")
    })
    public ResponseEntity<?> login(@RequestBody @Valid UserAuthRequest userAuthRequest) {
        UserAuthResponse authResponse = userService.login(userAuthRequest);
        authResponse.setToken(jwtTokenUtil.generateToken(authResponse.getEmail()));
        log.info("User logged in: {}", authResponse.getEmail());
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/register")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User successfully registered"),
            @ApiResponse(responseCode = "404", description = "Not Found - User with this email already exists"),
            @ApiResponse(responseCode = "400", description = "Bad Request - Invalid input data"),
    })
    public ResponseEntity<?> register(@RequestBody @Valid SaveUserRequest saveUserRequest) {
        userService.register(saveUserRequest);
        log.info("User registered: {}", saveUserRequest.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body("User successfully registered");
    }

    @PutMapping("/edit-profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User profile updated successfully"),
            @ApiResponse(responseCode = "201", description = "User profile updated successfully. Please verify your email"),
            @ApiResponse(responseCode = "400", description = "Bad Request - Invalid image format or other input errors"),
    })
    public ResponseEntity<?> editProfile(@AuthenticationPrincipal CurrentUser currentUser,
                                         @RequestBody @Valid UserEditRequest userEditRequest,
                                         @RequestParam("image") MultipartFile image) {
        boolean isEmailChanged = userService.editUser(currentUser.getUser(), userEditRequest, image);
        if (isEmailChanged) {
            return ResponseEntity.status(HttpStatus.CREATED).body("User edited successfully. Please verify your email");
        }
        log.info("User edited profile: {}", userEditRequest.getEmail());
        return ResponseEntity.status(HttpStatus.OK).body("User edited successfully");
    }

    @GetMapping("/verify")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User account verified successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request - Invalid or missing email/token"),
            @ApiResponse(responseCode = "404", description = "Not Found - User with the provided email does not exist"),
            @ApiResponse(responseCode = "409", description = "Conflict - Invalid verification token")
    })
    public ResponseEntity<String> verify(@RequestParam String email,
                                         @RequestParam String token) {
        userService.verifyUserAccount(email, token);
        return ResponseEntity.ok("User verified");
    }

    @PostMapping("/create-card")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Card created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request - Card number already exists or card count exceeds limit"),
            @ApiResponse(responseCode = "409", description = "Conflict - Card already exists or card count exceeds 4")
    })
    public ResponseEntity<String> createCard(@Valid @RequestBody SaveCardRequestRest cardRequest) {
        cardService.save(cardRequest);
        log.info("Card created {} ", cardRequest.getCardNumber());
        return ResponseEntity.ok("Card created successfully");
    }


    @DeleteMapping("/delete-card")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Card deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Card not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden - You are not the owner of this card")
    })
    public ResponseEntity<?> deleteCard(@AuthenticationPrincipal CurrentUser user,
                                        @RequestParam("cardNumber") String cardNumber) {
        cardService.deleteCardByCardNumber(user.getUser().getEmail(), cardNumber);
        log.info("Card deleted: {}", cardNumber);
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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "403", description = "Unauthorized - Old Password is incorrect"),
            @ApiResponse(responseCode = "400", description = "Bad Request - Confirm Password does not match"),
            @ApiResponse(responseCode = "200", description = "Ok - Password changed successfully ")
    })
    public ResponseEntity<String> changePassword(@AuthenticationPrincipal CurrentUser user,
                                                 @RequestBody PasswordChangeRequest passwordChangeRequest) {
        userService.changePassword(user.getUser(), passwordChangeRequest);
        log.info("Password changed: {}", passwordChangeRequest.getNewPassword());
        return ResponseEntity.ok("Password changed successfully");
    }

    @DeleteMapping("/delete-profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Card deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Unauthorized - Password is incorrect")
    })
    public ResponseEntity<?> deleteProfile(@AuthenticationPrincipal CurrentUser currentUser,
                                           @RequestBody PasswordConfirmationDto passwordConfirmationDto) {
        userService.delete(currentUser.getUser(), passwordConfirmationDto.getPassword(), passwordConfirmationDto.getConfirmPassword());
        log.info("Profile deleted: {}", passwordConfirmationDto.getPassword());
        return ResponseEntity.ok().build();
    }

}