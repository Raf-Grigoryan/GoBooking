package org.example.gobooking.dto.booking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.gobooking.dto.work.ServiceResponse;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SelectTimeResponse {

    private ServiceResponse serviceResponse;
    private List<LocalTime> availableSlots;
    private Date bookingDate;
    private int workerId;
}
