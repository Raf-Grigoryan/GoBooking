package org.example.gobookingrest.endpoint;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.gobookingcommon.dto.booking.SaveBookingRequestForRest;
import org.example.gobookingcommon.dto.booking.SelectTimeInformationForRest;
import org.example.gobookingcommon.dto.company.CompanyAndWorkersDto;
import org.example.gobookingcommon.dto.company.CompanyResponse;
import org.example.gobookingcommon.dto.work.WorkerAndServicesDto;
import org.example.gobookingcommon.service.*;
import org.example.gobookingrest.security.CurrentUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Date;


@RestController
@RequiredArgsConstructor
@RequestMapping("/booking")
@Slf4j
public class BookingEndpoint {

    private final CompanyService companyService;

    private final UserService userService;

    private final WorkService workService;

    private final BookingService bookingService;

    private final CardService cardService;

    @GetMapping("/search-company")
    public ResponseEntity<?> searchCompany(@RequestParam(value = "keyword", defaultValue = "") String keyword,
                                           @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                           @RequestParam(value = "pageSize", defaultValue = "8") int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        Page<CompanyResponse> companyResponses;
        if (keyword.isEmpty()) {
            companyResponses = companyService.getAllCompanies(pageRequest);
        } else {
            companyResponses = companyService.companyByKeyword(keyword, pageRequest);
        }
        return ResponseEntity.ok().body(companyResponses);
    }

    @GetMapping("/single-company")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "404", description = "Not Found - Company not found")
    })
    public ResponseEntity<?> singleCompany(@RequestParam(value = "id") int id) {
        CompanyAndWorkersDto dto = CompanyAndWorkersDto.builder()
                .company(companyService.getCompanyById(id))
                .workers(userService.workersByCompanyId(id))
                .build();

        return ResponseEntity.ok(dto);
    }


    @GetMapping("/worker-services")
    public ResponseEntity<?> workerServices(@RequestParam("workerId") int workerId) {
        WorkerAndServicesDto workerAndServicesDto = WorkerAndServicesDto.builder()
                .worker(userService.getWorkerById(workerId))
                .services(workService.getServicesByWorkerId(workerId))
                .build();
        return ResponseEntity.ok(workerAndServicesDto);
    }

    @GetMapping("/select-time")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "409", description = "There may be insufficient funds on your card"),
            @ApiResponse(responseCode = "200",description = "Successful retrieval of available booking times and payment options")
    })
    public ResponseEntity<?> confirm(@RequestParam("workerId") int workerId,
                                     @RequestParam("serviceId") int serviceId,
                                     @RequestParam(value = "bookingDate", defaultValue = "") @DateTimeFormat(pattern = "yyyy-MM-dd") Date bookingDate,
                                     @AuthenticationPrincipal CurrentUser currentUser) {
        SelectTimeInformationForRest selectTimeInformationForRest = SelectTimeInformationForRest.builder()
                .selectTimeResponse(bookingService.getSelectTimeByWorkerIdAndServiceId(workerId, serviceId, bookingDate))
                .bookingDate(bookingDate)
                .cards(cardService.getCardsByUserId(currentUser.getUser().getId()))
                .build();
        return ResponseEntity.ok(selectTimeInformationForRest);
    }

    @PostMapping("/save-booking")
    public ResponseEntity<?> saveBooking(@AuthenticationPrincipal CurrentUser currentUser,
                                         @RequestBody @Valid SaveBookingRequestForRest saveBookingRequestForRest) {
        bookingService.save(saveBookingRequestForRest.getSaveBookingRequest(), currentUser.getUser(), saveBookingRequestForRest.getBookingDate(), saveBookingRequestForRest.getCard());
        log.info("Booking saved");
        return ResponseEntity.ok().build();
    }

}
