package org.example.gobookingweb.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.gobookingcommon.dto.admin.AdminAnalyticDto;
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
    private final CompanyService companyService;
    private final AdminService adminService;

    @GetMapping("/promotion-request")
    public String promotionRequest(ModelMap modelMap,
                                   @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                   @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                   @RequestParam(value = "keyword", defaultValue = "") String keyword) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        Page<PromotionRequestDto> promotionRequestDtoList;

        if (keyword != null && !keyword.isEmpty()) {
            promotionRequestDtoList = promotionRequestsService.getAllPromotionsByRequesterEmail(pageRequest, keyword);
        } else {
            promotionRequestDtoList = promotionRequestsService.getAllPromotions(pageRequest);
        }

        int totalPages = promotionRequestDtoList.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .toList();
            modelMap.addAttribute("pageNumbers", pageNumbers);
        }
        modelMap.addAttribute("promotionRequestDtoList", promotionRequestDtoList);
        return "/admin/promotion_request";
    }

    @PostMapping("/agree")
    public String promotionRequestsAgree(@RequestParam("id") int id,
                                         @RequestParam("agree") boolean agree) {
        promotionRequestsService.agree(id, agree);
        log.info("Promotion request with ID: {} processed with decision: {}", id, agree);
        return "redirect:/admin/promotion-request-dashboard";
    }

    @GetMapping("/create-subscription")
    public String createSubscriptionPage() {
        return "/subscription/create-subscription";
    }

    @PostMapping("/create-subscription")
    public String createSubscription(@Valid @ModelAttribute SaveSubscriptionRequest subscriptionRequest) {
        subscriptionService.save(subscriptionRequest);
        log.info("Subscription created successfully.");
        return "redirect:/";
    }

    @GetMapping("/analytics")
    public String analytics(ModelMap modelMap) {
        AdminAnalyticDto adminAnalyticDto = adminService.getadminAnalyticDto();
        modelMap.addAttribute("adminAnalyticDto", adminAnalyticDto);
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
        log.info("Company deleted successfully.");
        return "redirect:/admin/not-valid-company";
    }
}
