package org.example.gobookingcommon.service;

import org.example.gobookingcommon.dto.booking.*;
import org.example.gobookingcommon.dto.subscription.BookingStatistics;
import org.example.gobookingcommon.entity.booking.Type;
import org.example.gobookingcommon.entity.user.User;
import java.util.Date;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface BookingService {

    SelectTimeResponse getSelectTimeByWorkerIdAndServiceId(int workerId, int serviceId, Date bookingDate);

    List<WorkerBookingResponse> clientFinishedBookings(int clientId, Type type);

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
