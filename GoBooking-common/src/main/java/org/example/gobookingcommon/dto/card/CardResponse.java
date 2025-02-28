package org.example.gobookingcommon.dto.card;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
@AllArgsConstructor
public class CardResponse {

    private String cardNumber;
    private String expirationDate;
    private String cvvCode;
    private double balance;
}
