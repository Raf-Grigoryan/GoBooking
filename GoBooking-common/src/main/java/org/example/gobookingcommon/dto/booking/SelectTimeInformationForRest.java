package org.example.gobookingcommon.dto.booking;


import lombok.Builder;
import lombok.Data;
import org.example.gobookingcommon.dto.card.CardResponse;

import java.util.Date;
import java.util.List;

@Data
@Builder
public class SelectTimeInformationForRest {

    SelectTimeResponse selectTimeResponse;

    Date bookingDate;

    List<CardResponse> cards;

}
