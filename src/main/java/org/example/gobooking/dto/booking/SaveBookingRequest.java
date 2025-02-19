package org.example.gobooking.dto.booking;

import lombok.Data;
import org.example.gobooking.entity.booking.PaymentMethod;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;

@Data
public class SaveBookingRequest {
    int serviceId;
    PaymentMethod paymentMethod;
    @DateTimeFormat(pattern = "HH:mm")
    LocalTime startTime;
}
