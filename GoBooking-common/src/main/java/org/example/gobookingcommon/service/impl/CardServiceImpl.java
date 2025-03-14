package org.example.gobookingcommon.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.gobookingcommon.customException.CardCountException;
import org.example.gobookingcommon.customException.CardOnlyExistException;
import org.example.gobookingcommon.customException.UnauthorizedCardAccessException;
import org.example.gobookingcommon.dto.card.CardResponse;
import org.example.gobookingcommon.dto.card.SaveCardRequest;
import org.example.gobookingcommon.dto.card.SaveCardRequestRest;
import org.example.gobookingcommon.entity.user.Card;
import org.example.gobookingcommon.mapper.CardMapper;
import org.example.gobookingcommon.repository.CardRepository;
import org.example.gobookingcommon.service.CardService;
import org.example.gobookingcommon.service.MailService;
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
        if (cardRepository.findByUserId(saveCardRequest.getUserId()).isEmpty()) {
            card.setPrimary(true);
        }
        cardRepository.save(card);
    }

    @Override
    public void save(SaveCardRequestRest saveCardRequestRest) {
        if (cardRepository.countByUserId(saveCardRequestRest.getUserId()) >= 4) {
            throw new CardCountException("Card count can't be more than 4");
        }
        if (cardRepository.existsCardByCardNumber(saveCardRequestRest.getCardNumber())) {
            throw new CardOnlyExistException("Card already exists");
        }
        Card card = cardMapper.toEntity(saveCardRequestRest);
        card.setBalance(BigDecimal.ZERO);
        if (cardRepository.findByUserId(saveCardRequestRest.getUserId()).isEmpty()) {
            card.setPrimary(true);
        }
        cardRepository.save(card);
    }

    @Override
    public List<CardResponse> getCardsByUserId(int userId) {
        return cardMapper.toDto(cardRepository.findCardByUserId(userId));
    }

    @Override
    @Transactional
    public void deleteCardByCardNumber(String email, String cardNumber) {
        Card cardInDb = cardRepository.findCardByCardNumber(cardNumber);
        if (cardInDb == null) {
            throw new EntityNotFoundException("Card not found");
        }

        if (cardInDb.getUser().getEmail() == null || !cardInDb.getUser().getEmail().equals(email)) {
            throw new UnauthorizedCardAccessException("You are not the owner of this card.");
        }
        cardRepository.deleteCardByCardNumber(cardNumber);
        mailService.sendMailForDeleteCard(email, cardNumber);
    }

    @Override
    public Card getCardByCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.isEmpty()) {
            throw new IllegalArgumentException("Card number cannot be null or empty");
        }
        return cardRepository.findCardByCardNumber(cardNumber);
    }

    @Override
    public void editCard(Card card) {
        if (card == null) {
            throw new IllegalArgumentException("Card cannot be null");
        }
        cardRepository.save(card);
    }

    @Override
    public Card getCardByUserIdAndMainIs(int userId, boolean mainIs) {

        return cardRepository.findCardByUserIdAndPrimary(userId, mainIs);
    }

    @Override
    public void save(Card card) {
        if (card == null) {
            throw new IllegalArgumentException("Card cannot be null");
        }
        cardRepository.save(card);
    }

    @Override
    public int getCardsCountByUserId(int userId) {
        return cardRepository.countByUserId(userId);
    }


}
