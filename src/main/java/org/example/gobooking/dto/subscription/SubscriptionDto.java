package org.example.gobooking.dto.subscription;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
public class SubscriptionDto {
    private String title;
    private String description;
    private int duration;
    private BigDecimal price;
    private int employeeCount;
}
