package org.example.gobookingcommon.dto.booking;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;

@Data
public class SaveBookingRequest {
    int serviceId;
    org.example.gobookingcommon.entity.booking.PaymentMethod paymentMethod;
    @DateTimeFormat(pattern = "HH:mm")
    LocalTime startTime;
}
