package org.example.gobooking.repository;

import org.example.gobooking.entity.booking.Booking;
import org.example.gobooking.entity.booking.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;


public interface BookingRepository extends JpaRepository<Booking, Integer> {

    List<Booking> findBookingByBookingDateAndServiceWorkerId(Date bookingDate, int workerId);

    List<Booking> getBookingByService_Worker_IdAndType(int workerId, Type type);

    @Query("SELECT COUNT(b) FROM Booking b WHERE b.service.worker.id = :workerId")
    int countBookingsByWorker(@Param("workerId") int workerId);


    @Query("SELECT COUNT(DISTINCT b.client.id) FROM Booking b WHERE b.service.worker.id = :workerId")
    int countDistinctClientsByWorker(@Param("workerId") int workerId);

    @Query("SELECT COALESCE(SUM(b.service.price), 0) FROM Booking b WHERE b.service.worker.id = :workerId AND b.type = 'FINISHED'")
    double sumTotalEarningsByWorker(@Param("workerId") int workerId);
}
