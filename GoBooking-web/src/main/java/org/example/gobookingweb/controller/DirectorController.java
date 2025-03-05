package org.example.gobookingweb.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.example.gobookingcommon.customException.CompanyNoCreateException;
import org.example.gobookingcommon.dto.company.CompanyDto;
import org.example.gobookingcommon.dto.company.SaveAddressRequest;
import org.example.gobookingcommon.dto.company.SaveCompanyRequest;
import org.example.gobookingcommon.dto.request.SaveRoleChangeRequest;
import org.example.gobookingcommon.dto.subscription.SubscriptionDto;
import org.example.gobookingcommon.dto.user.UserDto;
import org.example.gobookingcommon.service.*;
import org.example.gobookingweb.security.CurrentUser;
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
        CompanyDto company = companyService.getCompanyDtoByDirector(currentUser.getUser());
        modelMap.addAttribute("company", company);
        return "/director/director";
    }

    @GetMapping("/create-company")
    public String createCompanyPage(ModelMap modelMap) {
        modelMap.put("countries", countryService.getAllCountries());
        return "/director/add-company";
    }

    @PostMapping("/create-company")
    public String createCompany(@Valid @ModelAttribute SaveCompanyRequest companyRequest,
                                @Valid @ModelAttribute SaveAddressRequest saveAddressRequest,
                                @RequestParam("image") MultipartFile image,
                                @AuthenticationPrincipal CurrentUser currentUser) {
        companyService.save(companyRequest, saveAddressRequest, image, currentUser.getUser());
        log.info("Company successfully created.");
        return "redirect:/director";
    }

    @GetMapping("/delete-company")
    public String deleteCompany(@RequestParam("id") int id) {
        companyService.deleteCompany(id);
        return "redirect:/director";
    }

    @GetMapping("/send-role-change-request")
    public String sendRoleChangeRequest(ModelMap modelMap,
                                        @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                        @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                        @RequestParam(value = "keyword", defaultValue = "") String keyword,
                                        @AuthenticationPrincipal CurrentUser currentUser) {
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
            return "/director/send_role_change_request";
        }
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
        List<SubscriptionDto> subscriptions = subscriptionService.getAllSubscriptions();
        modelMap.addAttribute("subscriptions", subscriptions);
        return "/subscription/subscription";
    }

    @GetMapping("/buy-subscription")
    public String buySubscriptionPage(@AuthenticationPrincipal CurrentUser currentUser, ModelMap modelMap, @RequestParam("subscriptionTitle") String title) {
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
        validSubscriptionService.save(companyService.getCompanyByDirector(currentUser.getUser()), title, cardNumber);
        log.info("Subscription purchased successfully.");
        return "redirect:/director";
    }

    @GetMapping("/edit-company")
    public String editCompanyPage(ModelMap modelMap, @RequestParam("companyId") int companyId){
        modelMap.put("company", companyService.getCompanyById(companyId));
        modelMap.put("countries", countryService.getAllCountries());
        return "/director/edit-company";
    }

    @PostMapping("/edit-company")
    public String editCompany(@Valid @ModelAttribute SaveCompanyRequest companyRequest,
                              @Valid @ModelAttribute SaveAddressRequest addressRequest,
                              @RequestParam("image") MultipartFile image,
                              @RequestParam("companyId") int companyId,
                              @RequestParam("addressId") int addressId){
        companyService.editCompany(companyRequest,companyId,image,addressRequest,addressId);
        log.info("Company edited successfully");
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