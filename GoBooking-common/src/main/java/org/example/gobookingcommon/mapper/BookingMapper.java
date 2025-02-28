package org.example.gobookingcommon.mapper;

import org.example.gobookingcommon.dto.booking.PendingBookingResponse;
import org.example.gobookingcommon.dto.booking.WorkerBookingResponse;
import org.example.gobookingcommon.entity.booking.Booking;
import org.example.gobookingcommon.service.UserService;
import org.example.gobookingcommon.service.WorkService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;


@Mapper(componentModel = "spring",
        uses = {WorkService.class, UserService.class},
        imports = {WorkService.class, UserService.class}
)
public interface BookingMapper {

    @Mapping(source = "service", target = "serviceResponse")
    WorkerBookingResponse toBookingResponse(Booking booking);

    List<WorkerBookingResponse> workerBookingResponses(List<Booking> bookings);

    @Mapping(source = "service", target = "serviceResponse")
    PendingBookingResponse toPendingBookingResponse(Booking booking);

}
