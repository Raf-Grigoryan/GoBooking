package org.example.gobooking.controller;

import lombok.RequiredArgsConstructor;
import org.example.gobooking.dto.company.CompanyResponse;
import org.example.gobooking.service.BookingService;
import org.example.gobooking.service.CompanyService;
import org.example.gobooking.service.UserService;
import org.example.gobooking.service.WorkService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
                          @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date bookingDate,
                          ModelMap modelmap) {
        modelmap.put("selectTimeInformation",
                bookingService.getSelectTimeByWorkerIdAndServiceId(workerId, serviceId, bookingDate));
        return "/booking/select-time";
    }

}
