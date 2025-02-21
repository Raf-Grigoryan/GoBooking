package org.example.gobooking.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.gobooking.dto.booking.SaveBookingRequest;
import org.example.gobooking.dto.booking.SelectTimeResponse;
import org.example.gobooking.entity.booking.Booking;
import org.example.gobooking.entity.booking.Type;
import org.example.gobooking.entity.user.User;
import org.example.gobooking.entity.work.Service;
import org.example.gobooking.entity.work.WorkGraphic;
import org.example.gobooking.repository.BookingRepository;
import org.example.gobooking.repository.ServiceRepository;
import org.example.gobooking.service.BookingService;
import org.example.gobooking.service.WorkGraphicService;
import org.example.gobooking.service.WorkService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;


@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final WorkGraphicService workGraphicService;

    private final WorkService workService;

    private final BookingRepository bookingRepository;

    private final ServiceRepository serviceRepository;

    @Override
    public SelectTimeResponse getSelectTimeByWorkerIdAndServiceId(int workerId, int serviceId, Date bookingDate) {
        if (bookingDate == null) {
            bookingDate = java.sql.Date.valueOf(LocalDate.now());
        }

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(bookingDate);
        String weekDay = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.ENGLISH).toUpperCase();

        WorkGraphic workGraphic = workGraphicService.getWorkGraphicByWorkerIdAndDayWeek(workerId, weekDay);
        List<LocalTime> availableSlots = new ArrayList<>();

        if (workGraphic != null && workGraphic.isActive()) {
            List<Booking> bookings = bookingRepository.findBookingByBookingDateAndServiceWorkerId(bookingDate, workerId);
            LocalTime startOfWork = workGraphic.getStartWorkDate();
            LocalTime endOfWork = workGraphic.getEndedWorkDate();

            Service service = workService.getServiceByIdForBooking(serviceId);
            int serviceDurationMinutes = service.getDuration();

            LocalTime currentSlot = startOfWork;

            while (!currentSlot.isAfter(endOfWork.minusMinutes(serviceDurationMinutes))) {
                boolean isSlotAvailable = true;

                for (Booking booking : bookings) {
                    LocalTime bookingStart = booking.getStartedTime();
                    LocalTime bookingEnd = booking.getEndedTime();

                    if (bookingStart.equals(currentSlot)) {
                        currentSlot = bookingEnd;
                        isSlotAvailable = false;
                        break;
                    }
                }
                if (isSlotAvailable) {
                    availableSlots.add(currentSlot);
                    currentSlot = currentSlot.plusMinutes(30);
                }
            }
        }

        SelectTimeResponse response = new SelectTimeResponse();
        response.setServiceResponse(workService.getById(serviceId));
        response.setAvailableSlots(availableSlots);
        response.setWorkerId(workerId);
        response.setBookingDate(bookingDate);

        return response;
    }

    @Override
    public void save(SaveBookingRequest saveBookingRequest, User user,Date bookingDate) {
        Date date = Objects.requireNonNullElseGet(bookingDate, Date::new);
        Optional<Service> service = serviceRepository.findById(saveBookingRequest.getServiceId());
        service.ifPresent(value -> bookingRepository.save(Booking.builder()
                .service(value)
                .client(user)
                .startedTime(saveBookingRequest.getStartTime())
                .endedTime(saveBookingRequest.getStartTime().plusMinutes(value.getDuration()))
                .paymentMethod(saveBookingRequest.getPaymentMethod())
                .type(Type.REJECTED)
                .bookingDate(date)
                .build()));

    }


}
