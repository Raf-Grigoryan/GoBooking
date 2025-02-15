package org.example.gobooking.service;

import org.example.gobooking.dto.booking.SelectTimeResponse;

import java.util.Date;

public interface BookingService {

    SelectTimeResponse getSelectTimeByWorkerIdAndServiceId(int workerId, int serviceId, Date bookingDate);
}
