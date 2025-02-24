package org.example.gobooking.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.gobooking.customException.CompanyNoCreateException;
import org.example.gobooking.dto.company.CompanyDto;
import org.example.gobooking.dto.company.SaveAddressRequest;
import org.example.gobooking.dto.company.SaveCompanyRequest;
import org.example.gobooking.dto.request.SaveRoleChangeRequest;
import org.example.gobooking.dto.subscription.SubscriptionDto;
import org.example.gobooking.dto.user.UserDto;
import org.example.gobooking.security.CurrentUser;
import org.example.gobooking.service.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/director")
@RequiredArgsConstructor
@Slf4j
public class DirectorController {

    private final CompanyService companyService;

    private final CountryService countryService;

    private final UserService userService;

    private final DirectorService directorService;

    private final SubscriptionService subscriptionService;

    private final CardService cardService;

    private final ValidSubscriptionService validSubscriptionService;

    private final BookingService bookingService;

    private final WorkService workService;


    @GetMapping
    public String getDirectorPage(@AuthenticationPrincipal CurrentUser currentUser, ModelMap modelMap) {
        log.info("Fetching director page for user: {}", currentUser.getUser().getName());
        CompanyDto company = companyService.getCompanyDtoByDirector(currentUser.getUser());
        modelMap.addAttribute("company", company);
        log.debug("Company fetched: {}", company);
        return "/director/director";
    }

    @GetMapping("/create-company")
    public String createCompanyPage(ModelMap modelMap) {
        log.info("Fetching create company page");
        modelMap.put("countries", countryService.getAllCountries());
        return "/director/add-company";
    }

    @PostMapping("/create-company")
    public String createCompany(@Valid @ModelAttribute SaveCompanyRequest companyRequest,
                                @Valid @ModelAttribute SaveAddressRequest saveAddressRequest,
                                @RequestParam("image") MultipartFile image,
                                @AuthenticationPrincipal CurrentUser currentUser) {
        log.info("Creating company with request: {}", companyRequest);
        companyService.save(companyRequest, saveAddressRequest, image, currentUser.getUser());
        log.debug("Company successfully created.");
        return "redirect:/director";
    }

    @GetMapping("/delete-company")
    public String deleteCompany(@RequestParam("id") int id) {
        log.info("Deleting company with ID: {}", id);
        companyService.deleteCompany(id);
        log.debug("Company with ID {} deleted.", id);
        return "redirect:/director";
    }

    @GetMapping("/send-role-change-request")
    public String sendRoleChangeRequest(ModelMap modelMap,
                                        @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                        @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                        @RequestParam(value = "keyword", defaultValue = "") String keyword,
                                        @AuthenticationPrincipal CurrentUser currentUser) {
        log.info("Fetching role change request page for director: {}", currentUser.getUser().getName());
        CompanyDto company = companyService.getCompanyDtoByDirector(currentUser.getUser());
        if (company != null) {
            PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
            Page<UserDto> userDtoList;
            if (keyword != null && !keyword.isEmpty()) {
                userDtoList = userService.getAllUsersByEmail(pageRequest, keyword);
            } else {
                userDtoList = userService.getAllUsers(pageRequest);
            }
            int totalPages = userDtoList.getTotalPages();
            if (totalPages > 0) {
                List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                        .boxed()
                        .toList();
                modelMap.addAttribute("pageNumbers", pageNumbers);
            }
            modelMap.addAttribute("companyId", company.getId());
            modelMap.addAttribute("userDtoList", userDtoList);
            log.debug("Role change request page loaded for company ID: {}", company.getId());
            return "/director/send_role_change_request";
        }
        log.error("Company not found for director: {}", currentUser.getUser().getName());
        throw new CompanyNoCreateException("Company not found");
    }

    @PostMapping("/send-role-change-request")
    public String sendRoleChangeRequest(@ModelAttribute SaveRoleChangeRequest roleChangeRequest) {
        log.info("Sending role change request: {}", roleChangeRequest);
        directorService.sendWorkRequest(roleChangeRequest);
        log.debug("Role change request sent.");
        return "redirect:/director/send-role-change-request";
    }

    @GetMapping("/subscription")
    public String getSubscriptionPage(ModelMap modelMap) {
        log.info("Fetching subscription page.");
        List<SubscriptionDto> subscriptions = subscriptionService.getAllSubscriptions();
        modelMap.addAttribute("subscriptions", subscriptions);
        log.debug("Subscriptions fetched: {}", subscriptions.size());
        return "/subscription/subscription";
    }

    @GetMapping("/buy-subscription")
    public String buySubscriptionPage(@AuthenticationPrincipal CurrentUser currentUser, ModelMap modelMap, @RequestParam("subscriptionTitle") String title) {
        log.info("Fetching subscription page for user: {} with title: {}", currentUser.getUser().getName(), title);
        if (cardService.getCardsCountByUserId(currentUser.getUser().getId()) == 0) {
            log.warn("No cards found for user: {}. Redirecting to card creation.", currentUser.getUser().getName());
            return "redirect:/create-card";
        }
        modelMap.addAttribute("cards", cardService.getCardsByUserId(currentUser.getUser().getId()));
        modelMap.addAttribute("subscription", subscriptionService.getSubscriptionDtoByTitle(title));
        return "/subscription/buy-subscription";
    }


    @PostMapping("/buy-subscription")
    public String buySubscription(@AuthenticationPrincipal CurrentUser currentUser, @RequestParam("subscriptionTitle") String title, @RequestParam("cardNumber") String cardNumber) {
        log.info("Buying subscription for user: {} with title: {} and card number: {}", currentUser.getUser().getName(), title, cardNumber);
        validSubscriptionService.save(companyService.getCompanyByDirector(currentUser.getUser()), title, cardNumber);
        log.debug("Subscription purchased successfully.");
        return "redirect:/director";
    }

    @GetMapping("/my-company")
    public String getMyCompanyPage(@AuthenticationPrincipal CurrentUser currentUser, ModelMap modelMap) {
        modelMap.put("companyResponse", directorService.getCompanyManagementByDirectorId(currentUser.getUser().getId()));
        return "/director/my-company";
    }

    @GetMapping("all-services")
    public String getAllServicesPage(@AuthenticationPrincipal CurrentUser currentUser, ModelMap modelMap) {
        modelMap.put("services", workService.getAllServicesByDirectorId(currentUser.getUser().getId()));
        return "/director/all-services";
    }

    @GetMapping("/all-bookings")
    public String getAllBookingsPage(@AuthenticationPrincipal CurrentUser currentUser, ModelMap modelMap) {
        modelMap.put("bookings", bookingService.getFinishedBookingsByDirectorId(currentUser.getUser().getId()));
        return "/director/all-bookings";
    }
}