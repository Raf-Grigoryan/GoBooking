package org.example.gobookingrest.endpoint;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.gobookingcommon.dto.booking.PendingBookingResponse;
import org.example.gobookingcommon.dto.work.*;
import org.example.gobookingcommon.service.BookingService;
import org.example.gobookingcommon.service.WorkGraphicService;
import org.example.gobookingcommon.service.WorkService;
import org.example.gobookingrest.security.CurrentUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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


    private final BookingService bookingService;


    @GetMapping("/my-work-graphic")
    public ResponseEntity<List<WorkGraphicResponse>> getMyWorkGraphic(@AuthenticationPrincipal CurrentUser user) {
        return ResponseEntity.ok(workGraphicService.getWorkGraphicsByWorkerId(user.getUser().getId()));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "409", description = "Conflict - You are not allowed to edit this work graphic"),
            @ApiResponse(responseCode = "404", description = "Not Found - Work graphic not found"),
            @ApiResponse(responseCode = "200", description = "Ok - Work graphic updated")
    })
    @PutMapping("/edit-work-graphic")
    public ResponseEntity<?> editWorkGraphic(@AuthenticationPrincipal CurrentUser user, @RequestBody @Valid EditWorkGraphicRequest editWorkGraphicRequest) {
        workGraphicService.editWorkGraphic(user.getUser().getId(), editWorkGraphicRequest);
        return ResponseEntity.ok("Work graphic updated");
    }

    @GetMapping("/my-services")
    public ResponseEntity<List<ServiceResponse>> getMyServices(@AuthenticationPrincipal CurrentUser user) {
        return ResponseEntity.ok(workService.getServicesByWorkerId(user.getUser().getId()));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ok - Service created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request - Invalid image format or validation error"),
    })
    @PostMapping("/create-service")
    public ResponseEntity<?> createService(@AuthenticationPrincipal CurrentUser user,
                                           @RequestBody @Valid CreateServiceRequest serviceRequest,
                                           @RequestParam("image") MultipartFile image) {
        serviceRequest.setWorkerId(user.getUser().getId());
        workService.save(serviceRequest, image);
        return ResponseEntity.ok("Service created");
    }

    @PutMapping("/edit-service")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ok -Service updated successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request - Invalid input or image format"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User is not allowed to edit this service"),
            @ApiResponse(responseCode = "404", description = "Not Found - Service  not found"),
    })
    public ResponseEntity<?> editService(@ModelAttribute @Valid EditServiceRequest editServiceRequest,
                                         @RequestParam("image") MultipartFile image) {
        workService.editService(editServiceRequest, image);
        return ResponseEntity.ok("Service updated");
    }

    @DeleteMapping("/delete-service/{id}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ok - Service deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Not Found - Service not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden - cannot  delete this service")
    })
    public ResponseEntity<?> deleteService(@AuthenticationPrincipal CurrentUser user, @PathVariable int id) {
        workService.deleteById(user.getUser().getId(), id);
        return ResponseEntity.ok("Service deleted");
    }


    @GetMapping("/my-unfinished-bookings")
    public ResponseEntity<Page<PendingBookingResponse>> myUnfinishedBookings(@AuthenticationPrincipal CurrentUser user,
                                                                             @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                                                             @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        Page<PendingBookingResponse> pendingBookingResponse = bookingService.getUnfinishedBookings(user.getUser().getId(), pageRequest);
        return ResponseEntity.ok(pendingBookingResponse);
    }

    @GetMapping("/my-finished-bookings")
    public ResponseEntity<Page<PendingBookingResponse>> myFinishedBookings(@AuthenticationPrincipal CurrentUser user,
                                                                           @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                                                           @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        Page<PendingBookingResponse> pendingBookingResponse = bookingService.getFinishedBookings(user.getUser().getId(), pageRequest);
        return ResponseEntity.ok(pendingBookingResponse);
    }

    @PutMapping("/reject/{bookingId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Bad Request - You are not allowed to reject this booking or Booking type is not approved"),
            @ApiResponse(responseCode = "200", description = "Ok - Booking rejected")
    })
    public ResponseEntity<Page<PendingBookingResponse>> reject(@AuthenticationPrincipal CurrentUser user,
                                                               @PathVariable("bookingId") int bookingId,
                                                               @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                                               @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        bookingService.reject(bookingId, user.getUser());
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        Page<PendingBookingResponse> pendingBookingResponse = bookingService.getFinishedBookings(user.getUser().getId(), pageRequest);
        return ResponseEntity.ok(pendingBookingResponse);
    }

    @PutMapping("/finished/{bookingId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Bad Request - You are not allowed to reject this booking or Booking type is not approved."),
            @ApiResponse(responseCode = "200", description = "Ok - Booking finished")
    })
    public ResponseEntity<Page<PendingBookingResponse>> finished(@AuthenticationPrincipal CurrentUser user,
                                                                 @PathVariable("bookingId") int bookingId,
                                                                 @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                                                 @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        bookingService.finished(bookingId, user.getUser());
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        Page<PendingBookingResponse> pendingBookingResponse = bookingService.getFinishedBookings(user.getUser().getId(), pageRequest);
        return ResponseEntity.ok(pendingBookingResponse);
    }


}
