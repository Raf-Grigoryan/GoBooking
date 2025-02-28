package org.example.gobookingcommon.dto.company;

import lombok.Builder;
import lombok.Data;
import org.example.gobookingcommon.dto.booking.WorkerBookingResponse;
import org.example.gobookingcommon.dto.subscription.BookingStatistics;
import org.example.gobookingcommon.dto.user.WorkerResponse;
import org.example.gobookingcommon.dto.work.ServiceResponse;

import java.util.List;

@Data
@Builder
public class CompanyManagement {

    private CompanyResponse company;
    private List<WorkerResponse> workers;
    private int serviceCount;
    private int bookingCount;
    private double monthEarning;
    private BookingStatistics companyBookingStatistics;
    private List<ServiceResponse> serviceList;
    private List<WorkerBookingResponse> finishedBookings;
}
