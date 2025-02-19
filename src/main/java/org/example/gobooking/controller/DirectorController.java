package org.example.gobooking.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.gobooking.customException.CompanyNoCreateException;
import org.example.gobooking.dto.company.CompanyDto;
import org.example.gobooking.dto.company.SaveAddressRequest;
import org.example.gobooking.dto.company.SaveCompanyRequest;
import org.example.gobooking.dto.request.SaveRoleChangeRequest;
import org.example.gobooking.dto.subscription.SubscriptionDto;
import org.example.gobooking.dto.user.UserDto;
import org.example.gobooking.entity.subscription.Subscription;
import org.example.gobooking.entity.user.Card;
import org.example.gobooking.security.CurrentUser;
import org.example.gobooking.service.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/director")
@RequiredArgsConstructor
public class DirectorController {

    private final CompanyService companyService;

    private final CountryService countryService;

    private final UserService userService;

    private final DirectorService directorService;

    private final SubscriptionService subscriptionService;

    private final CardService cardService;

    private final ValidSubscriptionService validSubscriptionService;


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
    public String createCompany(@Valid @ModelAttribute SaveCompanyRequest companyRequest, @Valid @ModelAttribute SaveAddressRequest saveAddressRequest) {
        companyService.save(companyRequest, saveAddressRequest);
        return "redirect:/director";
    }

    @GetMapping("/delete-company")
    public String deleteCompany(@RequestParam("id") int id) {
        System.out.println(id);
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
        directorService.sendWorkRequest(roleChangeRequest);
        return "redirect:/director/send-role-change-request";
    }

    @GetMapping("/subscription")
    public String getSubscriptionPage(ModelMap modelMap) {
        List<SubscriptionDto> subscriptions = subscriptionService.getAllSubscriptions();
        modelMap.addAttribute("subscriptions", subscriptions);
        return "/subscription/subscription";
    }

    @GetMapping("/buy-subscription")
    public String BuySubscriptionPage(@AuthenticationPrincipal CurrentUser currentUser, ModelMap modelMap, @RequestParam ("subscriptionTitle") String title ) {
        modelMap.addAttribute("cards", cardService.getCardsByUserId(currentUser.getUser().getId()));
        modelMap.addAttribute("subscription",subscriptionService.getSubscriptionDtoByTitle(title));
        return "/subscription/buy-subscription";
    }

    @Transactional
    @PostMapping("/buy-subscription")
    public String BuySubscription(@AuthenticationPrincipal CurrentUser currentUser,  @RequestParam ("subscriptionTitle") String title, @RequestParam("cardNumber") String cardNumber) {
        Subscription subscription = subscriptionService.getSubscriptionByTitle(title);
        Card card = cardService.getCardByCardNumber(cardNumber);
        validSubscriptionService.save(companyService.getCompanyByDirector(currentUser.getUser()),subscription,card);
        return "redirect:/director";
    }

}