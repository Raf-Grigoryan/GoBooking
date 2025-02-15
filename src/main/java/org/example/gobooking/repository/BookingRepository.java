package org.example.gobooking.repository;

import org.example.gobooking.entity.booking.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;


public interface BookingRepository extends JpaRepository<Booking, Integer> {

    List<Booking> findBookingByBookingDate(Date bookingDate);
}
