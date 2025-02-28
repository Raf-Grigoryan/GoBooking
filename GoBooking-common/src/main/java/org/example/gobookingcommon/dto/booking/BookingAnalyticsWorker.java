package org.example.gobookingcommon.dto.booking;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookingAnalyticsWorker {
    private int clientCount;
    private int bookingCount;
    private double totalEarnings;
    private double potentialEarnings;
}
