package org.example.gobooking.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.gobooking.customException.CardCountException;
import org.example.gobooking.customException.CardOnlyExistException;
import org.example.gobooking.dto.card.CardResponse;
import org.example.gobooking.dto.card.SaveCardRequest;
import org.example.gobooking.entity.user.Card;
import org.example.gobooking.mapper.CardMapper;
import org.example.gobooking.repository.CardRepository;
import org.example.gobooking.service.CardService;
import org.example.gobooking.service.MailService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;

    private final CardMapper cardMapper;

    private final MailService mailService;


    @Override
    public void save(SaveCardRequest saveCardRequest) {
        if (cardRepository.countByUserId(saveCardRequest.getUserId()) >= 4) {
            throw new CardCountException("Card count can't be more than 4");
        }
        if (cardRepository.existsCardByCardNumber(saveCardRequest.getCardNumber())) {
            throw new CardOnlyExistException("Card already exists");
        }
        Card card = cardMapper.toEntity(saveCardRequest);
        card.setBalance(BigDecimal.ZERO);
        cardRepository.save(card);
    }

    @Override
    public List<CardResponse> getCardsByUserId(int userId) {
        return cardMapper.toDto(cardRepository.findCardByUserId(userId));
    }

    @Override
    @Transactional
    public void deleteCardByCardNumber(String email, String cardNumber) {
        cardRepository.deleteCardByCardNumber(cardNumber);
        mailService.sendMailForDeleteCard(email, cardNumber);
    }

    @Override
    public Card getCardByCardNumber(String cardNumber) {
        return cardRepository.findCardByCardNumber(cardNumber);
    }

    @Override
    public void editCard(Card card) {
        cardRepository.save(card);
    }
}
