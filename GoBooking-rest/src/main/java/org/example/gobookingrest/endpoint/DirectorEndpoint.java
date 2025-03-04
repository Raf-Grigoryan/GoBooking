package org.example.gobookingrest.endpoint;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.gobookingcommon.dto.company.CompanyDto;
import org.example.gobookingcommon.dto.company.CreateCompanyRequest;

import org.example.gobookingcommon.dto.request.SaveRoleChangeRequest;
import org.example.gobookingcommon.dto.subscription.SubscriptionDto;
import org.example.gobookingcommon.dto.subscription.SubscriptionDtoRest;
import org.example.gobookingcommon.dto.user.UserDto;
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

import java.util.ArrayList;
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

    @GetMapping
    public ResponseEntity<CompanyDto> getDirectorPage(@AuthenticationPrincipal CurrentUser currentUser) {
        log.info("Fetching director data for user: {}", currentUser.getUser().getName());
        CompanyDto company = companyService.getCompanyDtoByDirector(currentUser.getUser());
        return ResponseEntity.ok(company);
    }

    @GetMapping("/create-company")
    public ResponseEntity<List<Country>> getCreateCompanyData() {
        List<Country> countries = countryService.getAllCountries();
        return ResponseEntity.ok(countries);
    }

    @PostMapping(value = "/create-company", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> createCompany(@RequestBody CreateCompanyRequest createCompanyRequest,
                                                @RequestParam(value = "image", required = false) MultipartFile image,
                                                @AuthenticationPrincipal CurrentUser currentUser) {
        log.info("Creating company with request: {}", createCompanyRequest);
        companyService.save(createCompanyRequest.getSaveCompanyRequest(),
                createCompanyRequest.getSaveAddressRequest(),
                image,
                currentUser.getUser());
        return ResponseEntity.ok("Company successfully created");
    }

    @DeleteMapping("/delete-company/{id}")
    public ResponseEntity<String> deleteCompany(@PathVariable int id) {
        companyService.deleteCompany(id);
        return ResponseEntity.ok("Company deleted successfully");
    }

    @GetMapping("/send-role-change-request")
    public ResponseEntity<List<UserDto>> sendRoleChangeRequest(@AuthenticationPrincipal CurrentUser currentUser,
                                                               @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                                               @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                                               @RequestParam(value = "keyword", defaultValue = "") String keyword) {
        log.info("Fetching role change request data for director: {}", currentUser.getUser().getName());
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        Page<UserDto> userDtoList = keyword.isEmpty() ? userService.getAllUsers(pageRequest) : userService.getAllUsersByEmail(pageRequest, keyword);
        return ResponseEntity.ok(userDtoList.getContent());
    }

    @PostMapping("/send-role-change-request")
    public ResponseEntity<String> sendRoleChangeRequest(@RequestBody SaveRoleChangeRequest roleChangeRequest) {
        directorService.sendWorkRequest(roleChangeRequest);
        return ResponseEntity.ok("Role change request sent successfully");
    }

    @GetMapping("/subscriptions")
    public ResponseEntity<List<SubscriptionDto>> getSubscriptions() {
        log.info("Fetching subscriptions");
        return ResponseEntity.ok(subscriptionService.getAllSubscriptions());
    }

    @PostMapping("/buy-subscription")
    public ResponseEntity<String> buySubscription(@AuthenticationPrincipal CurrentUser currentUser,
                                                  @RequestBody SubscriptionDtoRest buySubscriptionDto) {
        validSubscriptionService.save(companyService.getCompanyByDirector(currentUser.getUser()), buySubscriptionDto.getSubscriptionTitle(), buySubscriptionDto.getCardNumber());
        return ResponseEntity.ok("Subscription purchased successfully");
    }

    @GetMapping("/edit-company/{companyId}")
    public ResponseEntity<List<?>> getEditCompany(@PathVariable("companyId") int companyId) {
        List<Object> response = new ArrayList<>();
        response.add(companyService.getCompanyById(companyId));
        response.add(countryService.getAllCountries());
        return ResponseEntity.ok(response);
    }

    @PutMapping(value = "/edit-company/{companyId}/{addressId}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> editCompany(@Valid @RequestBody CreateCompanyRequest companyRequest,
                                              @RequestParam(value = "image", required = false) MultipartFile image,
                                              @PathVariable("companyId") int companyId,
                                              @PathVariable("addressId") int addressId) {
        companyService.editCompany(companyRequest.getSaveCompanyRequest(), companyId, image, companyRequest.getSaveAddressRequest(), addressId);
        return ResponseEntity.ok("Company updated successfully");
    }

    @GetMapping("/my-company")
    public ResponseEntity<?> getMyCompanyPage(@AuthenticationPrincipal CurrentUser currentUser) {
        return ResponseEntity.ok(directorService.getCompanyManagementByDirectorId(currentUser.getUser().getId()));
    }

    @GetMapping("/all-services")
    public ResponseEntity<?> getAllServices(@AuthenticationPrincipal CurrentUser currentUser) {
        return ResponseEntity.ok(workService.getAllServicesByDirectorId(currentUser.getUser().getId()));
    }

    @GetMapping("/all-bookings")
    public ResponseEntity<?> getAllBookings(@AuthenticationPrincipal CurrentUser currentUser) {
        return ResponseEntity.ok(bookingService.getFinishedBookingsByDirectorId(currentUser.getUser().getId()));
    }
}
