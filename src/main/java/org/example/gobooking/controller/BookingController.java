package org.example.gobooking.controller;

import lombok.RequiredArgsConstructor;
import org.example.gobooking.dto.booking.SaveBookingRequest;
import org.example.gobooking.dto.company.CompanyResponse;
import org.example.gobooking.security.CurrentUser;
import org.example.gobooking.service.BookingService;
import org.example.gobooking.service.CompanyService;
import org.example.gobooking.service.UserService;
import org.example.gobooking.service.WorkService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/booking")
@RequiredArgsConstructor
public class BookingController {

    private final CompanyService companyService;

    private final UserService userService;

    private final WorkService workService;

    private final BookingService bookingService;

    @GetMapping("/search-company")
    public String searchCompany(@RequestParam(value = "keyword", defaultValue = "") String keyword, ModelMap modelmap,
                                @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                @RequestParam(value = "pageSize", defaultValue = "8") int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        Page<CompanyResponse> companyResponses;
        if (keyword.isEmpty()) {
            companyResponses = companyService.getAllCompanies(pageRequest);
        } else {
            companyResponses = companyService.companyByKeyword(keyword, pageRequest);
        }

        int totalPages = (companyResponses.getTotalPages());
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .toList();
            modelmap.put("pageNumbers", pageNumbers);
        }
        modelmap.put("companyResponses", companyResponses);
        return "/booking/companies";
    }

    @GetMapping("/single-company")
    public String singleCompany(@RequestParam(value = "id") int id, ModelMap modelmap) {
        modelmap.put("company", companyService.getCompanyById(id));
        modelmap.put("workers", userService.workersByCompanyId(id));
        return "/booking/single-company";
    }

    @GetMapping("/worker-services")
    public String workerServices(@RequestParam("workerId") int workerId, ModelMap modelmap) {
        modelmap.put("services", workService.getServicesByWorkerId(workerId));
        modelmap.put("worker", userService.getWorkerById(workerId));
        return "/booking/worker-services";
    }

    @GetMapping("/select-time")
    public String confirm(@RequestParam("workerId") int workerId,
                          @RequestParam("serviceId") int serviceId,
                          @RequestParam(value = "bookingDate", defaultValue = "") @DateTimeFormat(pattern = "yyyy-MM-dd") Date bookingDate,
                          ModelMap modelmap) {
        modelmap.put("selectTimeInformation",
                bookingService.getSelectTimeByWorkerIdAndServiceId(workerId, serviceId, bookingDate));
        modelmap.put("bookingDate", bookingDate);
        return "/booking/select-time";
    }

    @PostMapping("/save-booking")
    public String saveBooking(@RequestParam(value = "bookingDate", defaultValue = "")
                              @DateTimeFormat(pattern = "yyyy-MM-dd") Date bookingDate,
                              @RequestParam("workerId") int workerId,
                              @ModelAttribute SaveBookingRequest saveBookingRequest,
                              @AuthenticationPrincipal CurrentUser currentUser) {
        System.out.println(bookingDate);
        bookingService.save(saveBookingRequest, currentUser.getUser(), bookingDate);
        if (bookingDate != null) {
            return "redirect:/booking/select-time?workerId=" + workerId + "&serviceId=" + saveBookingRequest.getServiceId() + "&bookingDate=" + bookingDate.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
        }
        return "redirect:/booking/select-time?workerId=" + workerId + "&serviceId=" + saveBookingRequest.getServiceId() + "&bookingDate=";
    }

}
