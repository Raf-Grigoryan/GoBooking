package org.example.gobooking.service;

import org.example.gobooking.dto.card.CardResponse;
import org.example.gobooking.dto.card.SaveCardRequest;
import org.example.gobooking.entity.user.Card;

import java.util.List;

public interface CardService {

    void save(SaveCardRequest saveCardRequest);

    List<CardResponse> getCardsByUserId(int userId);

    void deleteCardByCardNumber(String email,String cardNumber);

    Card getCardByCardNumber(String cardNumber);
}
