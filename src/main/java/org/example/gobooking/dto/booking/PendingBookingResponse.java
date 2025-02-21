package org.example.gobooking.dto.booking;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import org.example.gobooking.dto.work.ServiceResponse;
import org.example.gobooking.entity.booking.PaymentMethod;
import org.example.gobooking.entity.booking.Type;
import org.example.gobooking.entity.user.User;

import java.time.LocalTime;
import java.util.Date;

@Data
public class PendingBookingResponse {

    private int id;
    private Type type;
    private int clientId;
    @Enumerated(EnumType.STRING)
    private User client;
    private PaymentMethod paymentMethod;
    private Date bookingDate;
    private LocalTime startedTime;
    private LocalTime endedTime;
    private ServiceResponse serviceResponse;
}


