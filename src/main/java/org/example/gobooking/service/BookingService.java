package org.example.gobooking.service;

import org.example.gobooking.dto.booking.*;
import org.example.gobooking.dto.subscription.BookingStatistics;
import org.example.gobooking.entity.booking.Type;
import org.example.gobooking.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Date;
import java.util.List;

public interface BookingService {

    SelectTimeResponse getSelectTimeByWorkerIdAndServiceId(int workerId, int serviceId, Date bookingDate);

    Page<WorkerBookingResponse> clientFinishedBookings(int clientId, Type type, PageRequest pageRequest);

    List<WorkerBookingResponse> clientFinishedBookings(int clientId, Type type);

    Page<PendingBookingResponse> getUnfinishedServices(int workerId, PageRequest pageRequest);

    BookingAnalyticsWorker getBookingAnalyticsWorker(int workerId);

    Page<PendingBookingResponse> getFinishedBookings(int workerId, PageRequest pageRequest);

    void save(SaveBookingRequest saveBookingRequest, User user, Date bookingDate, String cardNumber);

    int getBookingCountByCompanyId(int companyId);

    double getMonthEarningByCompanyId(int companyId);

    BookingStatistics getRandomServicesByCompanyId(int companyId);

    List<WorkerBookingResponse> getFinishedBookingsByCompanyId(int companyId);

    List<WorkerBookingResponse> getFinishedBookingsByDirectorId(int directorId);

    void reject(int bookingId);

    void finished(int bookingId);

    double getSumTotalEarningsByWorkerWhereTypeApproved(int workerId);

    double getSumTotalEarningsByWorkerWhereTypeFinished(int workerId);
}
