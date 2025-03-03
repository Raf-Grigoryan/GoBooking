package org.example.gobookingweb.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.gobookingcommon.dto.booking.PendingBookingResponse;
import org.example.gobookingcommon.dto.work.CreateServiceRequest;
import org.example.gobookingcommon.dto.work.EditServiceRequest;
import org.example.gobookingcommon.dto.work.EditWorkGraphicRequest;
import org.example.gobookingcommon.service.BookingService;
import org.example.gobookingcommon.service.WorkGraphicService;
import org.example.gobookingcommon.service.WorkService;
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
@RequestMapping("/worker")
@RequiredArgsConstructor
@Slf4j
public class WorkerController {


    private final WorkGraphicService workGraphicService;

    private final WorkService workService;

    private final BookingService bookingService;


    @GetMapping("/my-work-graphic")
    public String seeMyWorkGraphic(@AuthenticationPrincipal CurrentUser user, ModelMap modelMap) {
        log.info("Worker {} accessing their work graphic.", user.getUser().getName());
        modelMap.put("workGraphic", workGraphicService.getWorkGraphicsByWorkerId(user.getUser().getId()));
        modelMap.put("weekdays", workGraphicService.weekDays());
        return "/worker/my-work-graphic";
    }

    @PostMapping("/edit-work-graphic")
    public String editWorkGraphic(@AuthenticationPrincipal CurrentUser user, @ModelAttribute EditWorkGraphicRequest editWorkGraphicRequest) {
        workGraphicService.editWorkGraphic(user.getUser().getId(), editWorkGraphicRequest);
        return "redirect:/worker/my-work-graphic";
    }

    @GetMapping("/my-services")
    public String myServices(@AuthenticationPrincipal CurrentUser user, ModelMap modelMap) {
        modelMap.put("services", workService.getServicesByWorkerId(user.getUser().getId()));
        return "/worker/my-services";
    }

    @GetMapping("/create-service")
    public String createService() {
        log.info("Accessing create service page for new service creation.");
        return "/worker/create-service";
    }

    @PostMapping("/create-service") //+R
    public String createService(@AuthenticationPrincipal CurrentUser user,
                                @ModelAttribute @Valid CreateServiceRequest serviceRequest,
                                @RequestParam("image") MultipartFile image) {
        serviceRequest.setWorkerId(user.getUser().getId());
        workService.save(serviceRequest, image);
        return "redirect:/worker/my-services";
    }

    @GetMapping("/delete-service")
    public String deleteService(@AuthenticationPrincipal CurrentUser user, @RequestParam("serviceId") int serviceId) {
        workService.deleteById(user.getUser().getId(), serviceId);
        return "redirect:/worker/my-services";
    }

    @GetMapping("/edit-service")
    public String editServicePage(@RequestParam("serviceId") int serviceId,
                                  ModelMap modelMap) {
        modelMap.put("service", workService.getById(serviceId));
        return "/worker/edit-service";
    }

    @PostMapping("/edit-service")
    public String editService(@AuthenticationPrincipal CurrentUser user,
                              @ModelAttribute @Valid EditServiceRequest editServiceRequest,
                              @RequestParam("image") MultipartFile image) {
        editServiceRequest.setWorkerId(user.getUser().getId());
        workService.editService(editServiceRequest, image);
        return "redirect:/worker/my-services";
    }

    @GetMapping("/my-unfinished-bookings")//+G
    public String myUnfinishedBookings(@AuthenticationPrincipal CurrentUser user, ModelMap modelMap,
                                       @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                       @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        getPageRequest(pageNumber, pageSize, user.getUser().getId(), modelMap);
        modelMap.put("sumTotalEarningsByWorkerWhereTypeApproved", bookingService.getSumTotalEarningsByWorkerWhereTypeApproved(user.getUser().getId()));
        return "/worker/my-bookings";
    }

    @GetMapping("/my-finished-bookings")//+G
    public String myFinishedBookings(@AuthenticationPrincipal CurrentUser user, ModelMap modelMap,
                                     @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                     @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        getPageRequest(pageNumber, pageSize, user.getUser().getId(), modelMap);
        modelMap.put("sumTotalEarningsByWorkerWhereTypeFinished", bookingService.getSumTotalEarningsByWorkerWhereTypeFinished(user.getUser().getId()));

        return "/worker/my-finished-bookings";
    }

    @PostMapping("/reject")//+G
    public String reject(@AuthenticationPrincipal CurrentUser user, ModelMap modelMap,
                         @RequestParam("bookingId") int bookingId,
                         @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                         @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        bookingService.reject(bookingId);
        getPageRequest(pageNumber, pageSize, user.getUser().getId(), modelMap);
        modelMap.put("sumTotalEarningsByWorkerWhereTypeApproved", bookingService.getSumTotalEarningsByWorkerWhereTypeApproved(user.getUser().getId()));
        return "/worker/my-bookings";
    }

    @PostMapping("/finished")//+G
    public String finished(@AuthenticationPrincipal CurrentUser user, ModelMap modelMap,
                           @RequestParam("bookingId") int bookingId,
                           @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                           @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        bookingService.finished(bookingId);
        getPageRequest(pageNumber, pageSize, user.getUser().getId(), modelMap);
        modelMap.put("sumTotalEarningsByWorkerWhereTypeApproved", bookingService.getSumTotalEarningsByWorkerWhereTypeApproved(user.getUser().getId()));
        return "/worker/my-bookings";
    }


    private void getPageRequest(int pageNumber, int pageSize, int userId, ModelMap modelMap) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        Page<PendingBookingResponse> pendingBookingResponses = bookingService.getFinishedBookings(userId, pageRequest);
        int totalPages = pendingBookingResponses.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .toList();
            modelMap.addAttribute("pageNumbers", pageNumbers);
        }
        modelMap.put("bookings", pendingBookingResponses);
        modelMap.put("analytics", bookingService.getBookingAnalyticsWorker(userId));
    }

}
