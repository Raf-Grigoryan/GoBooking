package org.example.gobookingweb.util;


import org.example.gobookingcommon.dto.booking.PendingBookingResponse;
import org.example.gobookingcommon.service.BookingService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.ui.ModelMap;

import java.util.List;
import java.util.stream.IntStream;


public class WorkerUtil {

    public static void getPageRequest(int pageNumber, int pageSize, int userId, ModelMap modelMap,
                                      BookingService bookingService, boolean isFinished) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        Page<PendingBookingResponse> pendingBookingResponses = isFinished
                ? bookingService.getFinishedBookings(userId, pageRequest)
                : bookingService.getUnfinishedBookings(userId, pageRequest);

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

    public static void getFinishedPageRequest(int pageNumber, int pageSize, int userId, ModelMap modelMap, BookingService bookingService) {
        getPageRequest(pageNumber, pageSize, userId, modelMap, bookingService, true);
    }

    public static void getUnfinishedPageRequest(int pageNumber, int pageSize, int userId, ModelMap modelMap, BookingService bookingService) {
        getPageRequest(pageNumber, pageSize, userId, modelMap, bookingService, false);
    }
}
