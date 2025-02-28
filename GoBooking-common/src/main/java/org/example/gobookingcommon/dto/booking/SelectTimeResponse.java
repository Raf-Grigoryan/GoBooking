package org.example.gobookingcommon.dto.booking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.gobookingcommon.dto.work.ServiceResponse;


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
