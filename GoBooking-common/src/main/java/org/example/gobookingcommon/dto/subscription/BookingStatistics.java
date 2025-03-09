package org.example.gobookingcommon.dto.subscription;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingStatistics {
    private int finishedBookingsCount;
    private int approvedBookingsCount;
    private int rejectedBookingsCount;
}
