package org.example.gobookingrest.endpointTest;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.gobookingcommon.dto.admin.AdminAnalyticDto;
import org.example.gobookingcommon.dto.company.CompanyForAdminDto;
import org.example.gobookingcommon.dto.request.Agree;
import org.example.gobookingcommon.dto.request.PromotionRequestDto;
import org.example.gobookingcommon.dto.subscription.SaveSubscriptionRequest;
import org.example.gobookingcommon.service.AdminService;
import org.example.gobookingcommon.service.CompanyService;
import org.example.gobookingcommon.service.PromotionRequestsService;
import org.example.gobookingcommon.service.SubscriptionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@Slf4j
public class AdminEndpoint {

    private final PromotionRequestsService promotionRequestsService;
    private final SubscriptionService subscriptionService;
    private final CompanyService companyService;
    private final AdminService adminService;

    @GetMapping("/promotion-request")
    public ResponseEntity<Page<PromotionRequestDto>> promotionRequest(
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
        return ResponseEntity.ok(promotionRequestDtoList);
    }

    @PostMapping("/agree")
    public ResponseEntity<?> promotionRequestsAgree(@RequestBody Agree agree) {
        promotionRequestsService.agree(agree.getId(), agree.isAgree());
        log.debug("Promotion request with ID: {} processed with decision: {}", agree.getId(), agree.isAgree());
        return ResponseEntity.ok().build();
    }


    @PostMapping("/create-subscription")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "409", description = "SubscriptionOnlyExistException"),
            @ApiResponse(responseCode = "200", description = "Subscription created successfully.")
    })
    public ResponseEntity<?> createSubscription(@Valid @ModelAttribute SaveSubscriptionRequest subscriptionRequest) {
        subscriptionService.save(subscriptionRequest);
        log.debug("Subscription created successfully.");
        return ResponseEntity.ok().build();
    }

    @GetMapping("/analytics")
    public ResponseEntity<AdminAnalyticDto> analytics() {
        AdminAnalyticDto adminAnalyticDto = adminService.getadminAnalyticDto();
        return ResponseEntity.ok(adminAnalyticDto);
    }


    @GetMapping("/valid-company")
    public ResponseEntity<Page<CompanyForAdminDto>> validCompany(@RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                                                 @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        Page<CompanyForAdminDto> companies = companyService.getAllCompaniesByValid(true, pageRequest);
        return ResponseEntity.ok(companies);
    }

    @GetMapping("/not-valid-company")
    public ResponseEntity<Page<CompanyForAdminDto>> notValidCompany(
            @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        Page<CompanyForAdminDto> companies = companyService.getAllCompaniesByValid(false, pageRequest);
        return ResponseEntity.ok(companies);
    }

    @DeleteMapping("/delete-company/{directorID}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Company is deleted"),
            @ApiResponse(responseCode = "404", description = "Company not found")
    })
    public ResponseEntity<?> deleteCompany(@PathVariable("directorID") int directorID) {
        adminService.deleteCompany(directorID);
        log.info("Company deleted successfully.");
        return ResponseEntity.ok().build();
    }
}
