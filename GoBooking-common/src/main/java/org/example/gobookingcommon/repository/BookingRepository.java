package org.example.gobookingcommon.repository;

import org.example.gobookingcommon.entity.booking.Booking;
import org.example.gobookingcommon.entity.booking.Type;
import org.example.gobookingcommon.entity.user.User;
import org.example.gobookingcommon.entity.work.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;


public interface BookingRepository extends JpaRepository<Booking, Integer> {

    List<Booking> findBookingByBookingDateAndServiceWorkerId(Date bookingDate, int workerId);

    Page<Booking> getBookingByService_Worker_IdAndType(int workerId, Type type, Pageable pageable);

    List<Booking> getBookingsByClient_IdAndType(int clientId, Type type);

    @Query("SELECT COUNT(b) FROM Booking b WHERE b.service.worker.id = :workerId AND b.type = 'FINISHED'")
    int countBookingsByWorker(@Param("workerId") int workerId);


    @Query("SELECT COUNT(DISTINCT b.client.id) FROM Booking b WHERE b.service.worker.id = :workerId")
    int countDistinctClientsByWorker(@Param("workerId") int workerId);

    @Query("SELECT COALESCE(SUM(b.service.price), 0) FROM Booking b WHERE b.service.worker.id = :workerId AND b.type = 'FINISHED'")
    double sumTotalEarningsByWorker(@Param("workerId") int workerId);

    @Query("SELECT COALESCE(SUM(b.service.price), 0) FROM Booking b WHERE b.service.worker.id = :workerId AND b.type = 'APPROVED'")
    double potentialEarnings(@Param("workerId") int workerId);


    @Query("SELECT COUNT(b) FROM Booking b WHERE b.service.worker.company.id = :companyId")
    int countBookingsByCompanyId(@Param("companyId") int companyId);

    @Query("SELECT COALESCE(SUM(b.service.price), 0) FROM Booking b " +
            "WHERE b.service.worker.company.id = :companyId " +
            "AND b.type = 'FINISHED' " +
            "AND FUNCTION('MONTH', b.bookingDate) = FUNCTION('MONTH', CURRENT_DATE) " +
            "AND FUNCTION('YEAR', b.bookingDate) = FUNCTION('YEAR', CURRENT_DATE)")
    double sumMonthlyEarningsByCompany(@Param("companyId") int companyId);

    @Query("SELECT COUNT(b) FROM Booking b WHERE b.type = 'REJECTED' AND b.service.worker.company.id = :companyId")
    int countRejectedBookings(@Param("companyId") int companyId);

    @Query("SELECT COUNT(b) FROM Booking b WHERE b.type = 'APPROVED' AND b.service.worker.company.id = :companyId")
    int countApprovedBookings(@Param("companyId") int companyId);

    @Query("SELECT COUNT(b) FROM Booking b WHERE b.type = 'FINISHED' AND b.service.worker.company.id = :companyId")
    int countFinishedBookings(@Param("companyId") int companyId);

    @Query("SELECT b FROM Booking b " +
            "JOIN b.service s " +
            "JOIN s.worker u " +
            "WHERE u.company.id = :companyId AND b.type = 'FINISHED' " +
            "ORDER BY b.id DESC limit 8")
    List<Booking> findFinishedBookingsByCompanyId(@Param("companyId") int companyId);

    @Query("SELECT b FROM Booking b " +
            "JOIN b.service s " +
            "JOIN s.worker u " +
            "WHERE u.company.director.id = :directorId " +
            "AND FUNCTION('YEAR', b.bookingDate) = FUNCTION('YEAR', CURRENT_DATE) " +
            "AND FUNCTION('MONTH', b.bookingDate) = FUNCTION('MONTH', CURRENT_DATE) " +
            "ORDER BY b.id DESC")
    List<Booking> findBookingsByDirectorIdInCurrentMonth(
            @Param("directorId") int directorId);



    @Query("SELECT SUM(b.service.price) FROM Booking b WHERE b.service.worker.id = :workerId AND b.type = 'APPROVED'")
    Optional<Double> sumTotalEarningsByWorkerWhereTypeApproved(@Param("workerId") int workerId);

    @Query("SELECT SUM(b.service.price) FROM Booking b WHERE b.service.worker.id = :workerId AND b.type = 'FINISHED'")
    Optional<Double> sumTotalEarningsByWorkerWhereTypeFinished(@Param("workerId") int workerId);

    boolean existsAllByBookingDateAndStartedTimeAndService_Worker(Date bookingDate, LocalTime startedTime, User worker);

}
