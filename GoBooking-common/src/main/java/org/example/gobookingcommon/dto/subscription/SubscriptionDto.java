package org.example.gobookingcommon.dto.subscription;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionDto {
    private String title;
    private String description;
    private int duration;
    private BigDecimal price;
    private int employeeCount;
}
