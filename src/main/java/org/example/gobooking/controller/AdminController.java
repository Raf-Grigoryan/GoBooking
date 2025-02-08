package org.example.gobooking.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
public class AdminController {

    private final PromotionRequestsService promotionRequestsService;
    private final SubscriptionService subscriptionService;

    @GetMapping("/panel-1")
    public String panel() {
        return "admin/profile-followers";
    }

    @GetMapping("/promotion-request-dashboard")
    public String promotionRequestDashboard(ModelMap modelMap,
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
        return "/admin/promotion_request_dashboard";
    }

    @PostMapping("/agree")
    public String promotionRequestsAgree(@RequestParam("id") int id,
                                         @RequestParam("agree") boolean agree) {
        promotionRequestsService.agree(id, agree);
        return "redirect:/admin/promotion-request-dashboard";
    }

    @GetMapping("/create-subscription")
    public String createSubscriptionPage() {
        return "/admin/createSubscription";
    }

    @PostMapping("/create-subscription")
    public String createSubscription(@Valid @ModelAttribute SaveSubscriptionRequest subscriptionRequest) {
        subscriptionService.save(subscriptionRequest);
        return "redirect:/";
    }
}
