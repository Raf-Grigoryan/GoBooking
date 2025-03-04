package org.example.gobookingcommon.dto.subscription;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionDtoRest {

    private String subscriptionTitle;
    private String cardNumber;
}
