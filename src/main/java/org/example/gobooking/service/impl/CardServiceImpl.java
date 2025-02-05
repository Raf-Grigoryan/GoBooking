package org.example.gobooking.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.gobooking.customException.CardCountException;
import org.example.gobooking.customException.CardOnlyExistException;
import org.example.gobooking.dto.card.SaveCardRequest;
import org.example.gobooking.entity.user.Card;
import org.example.gobooking.mapper.CardMapper;
import org.example.gobooking.repository.CardRepository;
import org.example.gobooking.service.CardService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {
    private final CardRepository cardRepository;
    private final CardMapper cardMapper;

    @Override
    public void save(SaveCardRequest saveCardRequest) {
        System.out.println(cardRepository.countByUserId(saveCardRequest.getUserId()));
        if(cardRepository.countByUserId(saveCardRequest.getUserId())>=4) {
           throw new CardCountException("Card count can't be more 4");
        }
        if (cardRepository.existsCardByCardNumber(saveCardRequest.getCardNumber())) {
            throw new CardOnlyExistException("Card only exist");
        }
        Card card = cardMapper.toEntity(saveCardRequest);
        card.setBalance(new BigDecimal(0));
        cardRepository.save(card);
    }


}
