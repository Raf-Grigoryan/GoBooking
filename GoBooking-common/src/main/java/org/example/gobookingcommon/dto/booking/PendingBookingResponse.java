package org.example.gobookingcommon.dto.booking;


import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import org.example.gobookingcommon.dto.work.ServiceResponse;
import org.example.gobookingcommon.entity.booking.PaymentMethod;
import org.example.gobookingcommon.entity.booking.Type;
import org.example.gobookingcommon.entity.user.User;

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


