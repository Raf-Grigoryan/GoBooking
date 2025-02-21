package org.example.gobooking.service;

import org.example.gobooking.dto.booking.SaveBookingRequest;
import org.example.gobooking.dto.booking.SelectTimeResponse;
import org.example.gobooking.entity.user.User;

import java.util.Date;

public interface BookingService {

    SelectTimeResponse getSelectTimeByWorkerIdAndServiceId(int workerId, int serviceId, Date bookingDate);

    void save(SaveBookingRequest saveBookingRequest, User user, Date bookingDate, String cardNumber);
}
