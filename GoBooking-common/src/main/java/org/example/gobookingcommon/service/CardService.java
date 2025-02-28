package org.example.gobookingcommon.service;


import org.example.gobookingcommon.dto.card.CardResponse;
import org.example.gobookingcommon.dto.card.SaveCardRequest;
import org.example.gobookingcommon.entity.user.Card;


import java.util.List;

public interface CardService {

    void save(org.example.gobookingcommon.dto.card.SaveCardRequest saveCardRequest);

    void save(org.example.gobookingcommon.entity.user.Card card);

    List<org.example.gobookingcommon.dto.card.CardResponse> getCardsByUserId(int userId);

    void deleteCardByCardNumber(String email,String cardNumber);

    Card getCardByCardNumber(String cardNumber);

    int getCardsCountByUserId(int userId);

    void editCard(Card card);

    Card getCardByUserIdAndMainIs(int userId, boolean mainIs);
}
