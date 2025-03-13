package org.example.gobookingrest.endpoint;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.gobookingcommon.dto.company.CompanyDto;
import org.example.gobookingcommon.dto.company.CreateCompanyRequest;
import org.example.gobookingcommon.dto.request.SaveRoleChangeRequest;
import org.example.gobookingcommon.dto.subscription.SubscriptionDto;
import org.example.gobookingcommon.dto.subscription.SubscriptionDtoRest;
import org.example.gobookingcommon.dto.user.UserDto;
import org.example.gobookingcommon.dto.work.DirectorServiceResponse;
import org.example.gobookingcommon.entity.company.Country;
import org.example.gobookingcommon.service.*;
import org.example.gobookingrest.security.CurrentUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/director")
@Slf4j
public class DirectorEndpoint {

    private final CompanyService companyService;

    private final CountryService countryService;

    private final UserService userService;

    private final DirectorService directorService;

    private final SubscriptionService subscriptionService;

    private final ValidSubscriptionService validSubscriptionService;

    private final BookingService bookingService;

    private final WorkService workService;

    @GetMapping("/company")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched the company details"),
    })
    public ResponseEntity<CompanyDto> getCompany(@AuthenticationPrincipal CurrentUser currentUser) {
        CompanyDto company = companyService.getCompanyDtoByDirector(currentUser.getUser());
        return ResponseEntity.ok(company);
    }

    @GetMapping("/countries")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All countries for create company"),
    })
    public ResponseEntity<List<Country>> getCountries() {
        List<Country> countries = countryService.getAllCountries();
        return ResponseEntity.ok(countries);
    }

    @PostMapping(value = "/create-company", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Company successfully created"),
            @ApiResponse(responseCode = "400", description = "Bad Request - Invalid input data"),
            @ApiResponse(responseCode = "409", description = "Conflict - Company already exists or address conflict"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error - Server side failure")
    })
    public ResponseEntity<String> createCompany(@RequestBody CreateCompanyRequest createCompanyRequest,
                                                @RequestParam(value = "image", required = false) MultipartFile image,
                                                @AuthenticationPrincipal CurrentUser currentUser) {
        companyService.save(createCompanyRequest.getSaveCompanyRequest(),
                createCompanyRequest.getSaveAddressRequest(),
                image,
                currentUser.getUser());
        log.info("Created company with request: {}", createCompanyRequest);
        return ResponseEntity.ok("Company successfully created");
    }

    @DeleteMapping("/delete-company/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Company deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request - Director does not match"),
            @ApiResponse(responseCode = "404", description = "Not Found - Company does not exist"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<String> deleteCompany(@PathVariable int id, @AuthenticationPrincipal CurrentUser currentUser) {
        companyService.deleteCompany(id, currentUser.getUser());
        log.info("Company deleted successfully");
        return ResponseEntity.ok("Company deleted successfully");
    }

    @GetMapping("/role-change-request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved users")
    })
    public ResponseEntity<List<UserDto>> roleChangeRequest(@RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                                           @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                                           @RequestParam(value = "keyword", defaultValue = "") String keyword) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        Page<UserDto> userDtoList = keyword.isEmpty() ? userService.getAllUsers(pageRequest) : userService.getAllUsersByEmail(pageRequest, keyword);
        return ResponseEntity.ok(userDtoList.getContent());
    }

    @PostMapping("/send-role-change-request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully sent the role change request"),
            @ApiResponse(responseCode = "400", description = "Bad Request - Subscription is not valid"),
    })
    public ResponseEntity<String> sendRoleChangeRequest(@RequestBody SaveRoleChangeRequest roleChangeRequest, @AuthenticationPrincipal CurrentUser currentUser) {
        directorService.sendWorkRequest(roleChangeRequest, currentUser.getUser());
        log.info("Role change request sent successfully");
        return ResponseEntity.ok("Role change request sent successfully");
    }

    @GetMapping("/subscriptions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of subscriptions")
    })
    public ResponseEntity<List<SubscriptionDto>> getSubscriptions() {
        return ResponseEntity.ok(subscriptionService.getAllSubscriptions());
    }

    @PostMapping("/buy-subscription")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subscription purchased successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request - Card not found or invalid request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Insufficient funds")
    })
    public ResponseEntity<String> buySubscription(@AuthenticationPrincipal CurrentUser currentUser,
                                                  @RequestBody SubscriptionDtoRest buySubscriptionDto) {
        validSubscriptionService.save(companyService.getCompanyByDirector(currentUser.getUser()), buySubscriptionDto.getSubscriptionTitle(), buySubscriptionDto.getCardNumber());
        log.info("Subscription purchased successfully");
        return ResponseEntity.ok("Subscription purchased successfully");
    }

    @PutMapping(value = "/edit-company/{companyId}/{addressId}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Company updated successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request - Invalid request body or parameters"),
            @ApiResponse(responseCode = "404", description = "Not Found - Company or Address not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden - User is not the director of the company")
    })
    public ResponseEntity<String> editCompany(@Valid @RequestBody CreateCompanyRequest companyRequest,
                                              @RequestParam(value = "image", required = false) MultipartFile image,
                                              @PathVariable("companyId") int companyId,
                                              @PathVariable("addressId") int addressId,
                                              @AuthenticationPrincipal CurrentUser currentUser) {
        companyService.editCompany(companyRequest.getSaveCompanyRequest(), companyId, image, companyRequest.getSaveAddressRequest(), addressId, currentUser.getUser());
        log.info("Company updated successfully");
        return ResponseEntity.ok("Company updated successfully");
    }

    @GetMapping("/my-company")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the company information"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User is not authenticated"),
            @ApiResponse(responseCode = "404", description = "Not Found - Company not found for the director"),
    })
    public ResponseEntity<?> getMyCompany(@AuthenticationPrincipal CurrentUser currentUser) {
        return ResponseEntity.ok(directorService.getCompanyManagementByDirectorId(currentUser.getUser().getId()));
    }

    @GetMapping("/all-services")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the company information"),
    })
    public ResponseEntity<List<DirectorServiceResponse>> getAllServices(@AuthenticationPrincipal CurrentUser currentUser) {
        return ResponseEntity.ok(workService.getAllServicesByDirectorId(currentUser.getUser().getId()));
    }

    @GetMapping("/all-bookings")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the finished bookings"),
    })
    public ResponseEntity<?> getAllBookings(@AuthenticationPrincipal CurrentUser currentUser) {
        return ResponseEntity.ok(bookingService.getFinishedBookingsByDirectorId(currentUser.getUser().getId()));
    }
}
