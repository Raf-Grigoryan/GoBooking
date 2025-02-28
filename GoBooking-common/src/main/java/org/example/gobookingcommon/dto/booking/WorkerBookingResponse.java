package org.example.gobookingcommon.dto.booking;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import org.example.gobookingcommon.dto.work.ServiceResponse;
import org.example.gobookingcommon.entity.booking.PaymentMethod;


import java.util.Date;

@Data
public class WorkerBookingResponse {

    private ServiceResponse serviceResponse;
    private Date bookingDate;
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

}
