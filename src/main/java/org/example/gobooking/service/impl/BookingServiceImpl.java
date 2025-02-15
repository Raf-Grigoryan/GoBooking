package org.example.gobooking.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.gobooking.dto.booking.SelectTimeResponse;
import org.example.gobooking.entity.booking.Booking;
import org.example.gobooking.entity.work.Service;
import org.example.gobooking.entity.work.WorkGraphic;
import org.example.gobooking.repository.BookingRepository;
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
            List<Booking> bookings = bookingRepository.findBookingByBookingDate(bookingDate);
            LocalTime startOfWork = workGraphic.getStartWorkDate();
            LocalTime endOfWork = workGraphic.getEndedWorkDate();

            Service service = workService.getServiceByIdForBooking(serviceId);
            int serviceDurationMinutes = service.getDuration();

            LocalTime currentSlot = startOfWork;

            while (!currentSlot.isAfter(endOfWork.minusMinutes(serviceDurationMinutes))) {
                LocalTime slotEnd = currentSlot.plusMinutes(serviceDurationMinutes);
                boolean isSlotAvailable = true;

                for (Booking booking : bookings) {
                    LocalTime bookingStart = booking.getStartedTime();
                    LocalTime bookingEnd = booking.getEndedTime();

                    if (!(slotEnd.isBefore(bookingStart) || currentSlot.isAfter(bookingEnd.minusMinutes(1)))) {
                        currentSlot = bookingEnd; // Перемещаем слот на конец забронированного времени
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

}
