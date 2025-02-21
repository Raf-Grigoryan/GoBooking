package org.example.gobooking.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.gobooking.dto.request.PromotionRequestDto;
import org.example.gobooking.dto.subscription.SaveSubscriptionRequest;
import org.example.gobooking.service.PromotionRequestsService;
import org.example.gobooking.service.SubscriptionService;
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
}
