package org.example.gobooking.mapper;

import org.example.gobooking.dto.booking.PendingBookingResponse;
import org.example.gobooking.dto.booking.WorkerBookingResponse;
import org.example.gobooking.entity.booking.Booking;
import org.example.gobooking.service.UserService;
import org.example.gobooking.service.WorkService;
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

    List<PendingBookingResponse> pendingBookingResponses(List<Booking> bookings);

}
