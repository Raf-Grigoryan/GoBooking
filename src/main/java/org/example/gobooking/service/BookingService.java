package org.example.gobooking.service;

import org.example.gobooking.dto.booking.*;
import org.example.gobooking.entity.booking.Type;
import org.example.gobooking.entity.user.User;

import java.util.Date;
import java.util.List;

public interface BookingService {

    SelectTimeResponse getSelectTimeByWorkerIdAndServiceId(int workerId, int serviceId, Date bookingDate);

    void save(SaveBookingRequest saveBookingRequest, User user, Date bookingDate);

    List<WorkerBookingResponse> clientFinishedBookings(int clientId, Type type);

    List<PendingBookingResponse> getUnfinishedServices(int workerId);

    BookingAnalyticsWorker getBookingAnalyticsWorker(int workerId);

    List<PendingBookingResponse> getFinishedBookings(int workerId);

    void save(SaveBookingRequest saveBookingRequest, User user, Date bookingDate, String cardNumber);
}
