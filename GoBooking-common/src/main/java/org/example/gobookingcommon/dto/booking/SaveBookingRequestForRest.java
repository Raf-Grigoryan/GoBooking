package org.example.gobookingcommon.dto.booking;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class SaveBookingRequestForRest {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/yy")
    Date bookingDate;
    int workerId;
    SaveBookingRequest saveBookingRequest;
    String card;

}
