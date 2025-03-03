package org.example.gobookingrest.endpoint;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.gobookingcommon.dto.work.*;
import org.example.gobookingcommon.service.WorkGraphicService;
import org.example.gobookingcommon.service.WorkService;
import org.example.gobookingrest.security.CurrentUser;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/worker")
@Slf4j
public class WorkerEndpoint {

    private final WorkGraphicService workGraphicService;

    private final WorkService workService;

    @GetMapping("/my-work-graphic")
    public ResponseEntity<List<WorkGraphicResponse>> getMyWorkGraphic(@AuthenticationPrincipal CurrentUser user) {
        return ResponseEntity.ok(workGraphicService.getWorkGraphicsByWorkerId(user.getUser().getId()));
    }

    @PutMapping("/edit-work-graphic")
    public ResponseEntity<?> editWorkGraphic(@AuthenticationPrincipal CurrentUser user, @RequestBody @Valid EditWorkGraphicRequest editWorkGraphicRequest) {
        workGraphicService.editWorkGraphic(user.getUser().getId(), editWorkGraphicRequest);
        return ResponseEntity.ok("Work graphic updated");
    }

    @GetMapping("/my-services")
    public ResponseEntity<List<ServiceResponse>> getMyServices(@AuthenticationPrincipal CurrentUser user) {
        return ResponseEntity.ok(workService.getServicesByWorkerId(user.getUser().getId()));
    }

    @PostMapping("/create-service")
    public ResponseEntity<?> createService(@AuthenticationPrincipal CurrentUser user,
                                           @RequestBody @Valid CreateServiceRequest serviceRequest,
                                           @RequestParam("image") MultipartFile image) {
        serviceRequest.setWorkerId(user.getUser().getId());
        workService.save(serviceRequest, image);
        return ResponseEntity.ok("Service created");
    }

    @PatchMapping("/edit-service")
    public ResponseEntity<?> editService(@ModelAttribute @Valid EditServiceRequest editServiceRequest,
                                         @RequestParam("image") MultipartFile image) {
        workService.editService(editServiceRequest, image);
        return ResponseEntity.ok("Service updated");
    }

    @DeleteMapping("/delete-service/{id}")
    public ResponseEntity<?> deleteService(@AuthenticationPrincipal CurrentUser user, @PathVariable int id) {
        workService.deleteById(user.getUser().getId(), id);
        return ResponseEntity.ok("Service deleted");
    }
}
