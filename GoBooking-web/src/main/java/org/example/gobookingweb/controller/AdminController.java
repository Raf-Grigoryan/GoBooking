package org.example.gobookingweb.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.example.gobookingcommon.dto.company.CompanyForAdminDto;
import org.example.gobookingcommon.dto.request.PromotionRequestDto;
import org.example.gobookingcommon.dto.subscription.SaveSubscriptionRequest;
import org.example.gobookingcommon.service.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final PromotionRequestsService promotionRequestsService;
    private final SubscriptionService subscriptionService;
    private final UserService userService;
    private final CompanyService companyService;
    private final ProjectFinanceService projectFinanceService;
    private final BookingBalanceService bookingBalanceService;
    private final AdminService adminService;

    @GetMapping("/panel-1")
    public String panel() {
        log.info("Admin accessed the panel-1 page.");
        return "admin/profile-followers";
    }

    @GetMapping("/promotion-request")
    public String promotionRequest(ModelMap modelMap,
                                   @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                   @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                   @RequestParam(value = "keyword", defaultValue = "") String keyword) {
        log.info("Fetching promotion requests: pageNumber = {}, pageSize = {}, keyword = {}", pageNumber, pageSize, keyword);

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        Page<PromotionRequestDto> promotionRequestDtoList;

        if (keyword != null && !keyword.isEmpty()) {
            promotionRequestDtoList = promotionRequestsService.getAllPromotionsByRequesterEmail(pageRequest, keyword);
            log.debug("Fetching promotions with keyword: {}", keyword);
        } else {
            promotionRequestDtoList = promotionRequestsService.getAllPromotions(pageRequest);
            log.debug("Fetching all promotion requests without a keyword.");
        }

        int totalPages = promotionRequestDtoList.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .toList();
            modelMap.addAttribute("pageNumbers", pageNumbers);
            log.debug("Total pages: {}", totalPages);
        }

        modelMap.addAttribute("promotionRequestDtoList", promotionRequestDtoList);
        return "/admin/promotion_request";
    }

    @PostMapping("/agree")
    public String promotionRequestsAgree(@RequestParam("id") int id,
                                         @RequestParam("agree") boolean agree) {
        log.info("Admin is processing promotion request: id = {}, agree = {}", id, agree);
        promotionRequestsService.agree(id, agree);
        log.debug("Promotion request with ID: {} processed with decision: {}", id, agree);
        return "redirect:/admin/promotion-request-dashboard";
    }

    @GetMapping("/create-subscription")
    public String createSubscriptionPage() {
        log.info("Admin accessed the create subscription page.");
        return "/subscription/create-subscription";
    }

    @PostMapping("/create-subscription")
    public String createSubscription(@Valid @ModelAttribute SaveSubscriptionRequest subscriptionRequest) {
        log.info("Creating a new subscription with details: {}", subscriptionRequest);
        subscriptionService.save(subscriptionRequest);
        log.debug("Subscription created successfully.");
        return "redirect:/";
    }

    @GetMapping("/analytics")
    public String analytics(ModelMap modelMap) {
        List<Integer> currentWeek = userService.analyticUsers();
        List<String> labels = List.of(
                "Jan", "Feb", "Mar", "Apr", "May", "Jun",
                "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
        );
        modelMap.addAttribute("currentWeek", currentWeek);
        modelMap.addAttribute("labels", labels);

        List<Integer> series = userService.getAllRolesUsersCount();
        modelMap.addAttribute("series", series);

        modelMap.addAttribute("companyValid", companyService.countCompaniesByValid(true));
        modelMap.addAttribute("companyNotValid", companyService.countCompaniesByValid(false));

        modelMap.addAttribute("projectFinance", projectFinanceService.getProjectFinance());
        modelMap.addAttribute("bookingBalance", bookingBalanceService.getBookingBalance());
        return "/admin/analytics";
    }

    @GetMapping("/valid-company")
    public String validCompany(ModelMap modelMap,
                               @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                               @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        Page<CompanyForAdminDto> companies =companyService.getAllCompaniesByValid(true,pageRequest);
        int totalPages = companies.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .toList();
            modelMap.addAttribute("pageNumbers", pageNumbers);
        }
        modelMap.addAttribute("companies", companies);
        return "/admin/valid-company";
    }

    @GetMapping("/not-valid-company")
    public String notValidCompany(ModelMap modelMap,
                               @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                               @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        Page<CompanyForAdminDto> companies =companyService.getAllCompaniesByValid(false,pageRequest);
        int totalPages = companies.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .toList();
            modelMap.addAttribute("pageNumbers", pageNumbers);
        }
        modelMap.addAttribute("companies", companies);
        return "/admin/not-valid-company";
    }

    @PostMapping("/delete-company")
    public String deleteCompany(@RequestParam("id") int directorID) {
        adminService.deleteCompany(directorID);
        return "redirect:/admin/not-valid-company";
    }
}
