package org.example.gobooking.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.gobooking.customException.CardOnlyExistException;
import org.example.gobooking.dto.card.SaveCardRequest;
import org.example.gobooking.entity.user.Card;
import org.example.gobooking.mapper.CardMapper;
import org.example.gobooking.repository.CardRepository;
import org.example.gobooking.service.CardService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {
    private final CardRepository cardRepository;
    private final CardMapper cardMapper;

    @Override
    public void save(SaveCardRequest saveCardRequest) {
        Optional<Card> cardDb = cardRepository.findByCardNumber(saveCardRequest.getCardNumber());
        if (cardDb.isEmpty()){
            Card card = cardMapper.toEntity(saveCardRequest);
            card.setBalance(new BigDecimal(0));
            cardRepository.save(card);
        }
        throw  new CardOnlyExistException("Card only exist");
    }


}
