package org.example.gobookingcommon.dto.subscription;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookingStatistics {
    private int finishedBookingsCount;
    private int approvedBookingsCount;
    private int rejectedBookingsCount;
}
