package org.example.gobookingrest.endpoint;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.gobookingcommon.dto.request.RoleChangeRequestDto;
import org.example.gobookingcommon.dto.request.SavePromotionRequest;
import org.example.gobookingcommon.service.PromotionRequestsService;
import org.example.gobookingcommon.service.RoleChangeRequestService;
import org.example.gobookingrest.security.CurrentUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Slf4j
public class UserEndpoint {

    private final PromotionRequestsService promotionRequestsService;

    private final RoleChangeRequestService roleChangeRequestService;

    @PostMapping("/send-promotion-request")
    public ResponseEntity<String> sendPromotionRequest(@RequestBody @Valid SavePromotionRequest savePromotionRequest) {
        promotionRequestsService.savePromotion(savePromotionRequest);
        return ResponseEntity.ok("Promotion request sent successfully");
    }

    @GetMapping("/role-change-acceptance")
    public ResponseEntity<Page<RoleChangeRequestDto>> getRoleChangeRequests(
            @AuthenticationPrincipal CurrentUser currentUser,
            @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {


        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        Page<RoleChangeRequestDto> roleChangeRequestDtoPage =
                roleChangeRequestService.findByEmployee(currentUser.getUser(), pageRequest);

        log.debug("Fetched {} role change requests for user {}.",
                roleChangeRequestDtoPage.getContent().size(), currentUser.getUser().getName());

        return ResponseEntity.ok(roleChangeRequestDtoPage);
    }

    @PostMapping("/agree/{companyId}/{agree}")
    public ResponseEntity<String> agreeToRoleChange(
            @AuthenticationPrincipal CurrentUser currentUser,
            @PathVariable("companyId") int companyId,
            @PathVariable("agree") String agree) {
        boolean agreeBoolean = "1".equals(agree) || Boolean.parseBoolean(agree);
        roleChangeRequestService.agree(companyId, agreeBoolean, currentUser.getUser());
        return ResponseEntity.ok("Role change request processed successfully");
    }

}
