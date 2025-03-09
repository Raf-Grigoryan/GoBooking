package org.example.gobookingcommon.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import jakarta.persistence.EntityNotFoundException;
import org.example.gobookingcommon.customException.CardCountException;
import org.example.gobookingcommon.customException.CardOnlyExistException;
import org.example.gobookingcommon.customException.UnauthorizedCardAccessException;
import org.example.gobookingcommon.dto.card.CardResponse;
import org.example.gobookingcommon.dto.card.SaveCardRequest;

import org.example.gobookingcommon.dto.card.SaveCardRequestRest;
import org.example.gobookingcommon.entity.user.Card;
import org.example.gobookingcommon.entity.user.User;

import org.example.gobookingcommon.mapper.CardMapper;
import org.example.gobookingcommon.repository.CardRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private CardMapper cardMapper;

    @Mock
    private MailServiceImpl mailService;

    @InjectMocks
    private CardServiceImpl cardService;

    private SaveCardRequest saveCardRequest;
    private List<Card> cardList;
    private List<CardResponse> cardResponseList;
    private Card card1;

    private final String validEmail = "user@example.com";
    private final String invalidEmail = "otheruser@example.com";
    private final String cardNumber = "1234567812345678";
    private int userId;

    @BeforeEach
    void setUp() {
        userId = 1;

        saveCardRequest = new SaveCardRequest();
        saveCardRequest.setUserId(1);
        saveCardRequest.setCardNumber("1234567812345678");

        User user = new User();

        card1 = new Card(); // Используем card1 как единственный объект.
        card1.setId(1);
        card1.setCardNumber("1234567812345678");
        card1.setExpirationDate(new Date());
        card1.setCvvCode("123");
        card1.setBalance(BigDecimal.ZERO);
        card1.setUser(user);
        card1.setPrimary(true);

        Card card2 = new Card();
        card2.setId(2);
        card2.setCardNumber("1234567812345679");
        card2.setExpirationDate(new Date());
        card2.setCvvCode("456");
        card2.setBalance(BigDecimal.ZERO);
        card2.setUser(user);

        cardList = List.of(card1, card2);

        CardResponse response1 = new CardResponse("1", "11/26", "254", 2.00);
        CardResponse response2 = new CardResponse("2", "10/25", "123", 3.00);

        cardResponseList = List.of(response1, response2);

    }

    @Test
    void testSave_ThrowsCardCountException_WhenUserHasFourCards() {
        when(cardRepository.countByUserId(saveCardRequest.getUserId())).thenReturn(4);

        assertThrows(CardCountException.class, () -> cardService.save(saveCardRequest));

        verify(cardRepository, never()).save(any(Card.class));
    }

    @Test
    void testSave_ThrowsCardOnlyExistException_WhenCardAlreadyExists() {
        when(cardRepository.existsCardByCardNumber(saveCardRequest.getCardNumber())).thenReturn(true);

        assertThrows(CardOnlyExistException.class, () -> cardService.save(saveCardRequest));

        verify(cardRepository, never()).save(any(Card.class));
    }

    @Test
    void testSave_SuccessfullySavesNewCard() {
        when(cardRepository.countByUserId(saveCardRequest.getUserId())).thenReturn(0);
        when(cardRepository.existsCardByCardNumber(saveCardRequest.getCardNumber())).thenReturn(false);
        when(cardMapper.toEntity(saveCardRequest)).thenReturn(card1);

        cardService.save(saveCardRequest);

        assertEquals(BigDecimal.ZERO, card1.getBalance());

        assertTrue(card1.isPrimary());

        verify(cardRepository, times(1)).save(card1);
    }

    @Test
    void testGetCardsByUserId_ReturnsCardList() {
        when(cardRepository.findCardByUserId(1)).thenReturn(cardList);
        when(cardMapper.toDto(cardList)).thenReturn(cardResponseList);

        List<CardResponse> result = cardService.getCardsByUserId(1);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("1", result.get(0).getCardNumber()); // Теперь cardNumber из CardResponse
        assertEquals("2", result.get(1).getCardNumber());

        verify(cardRepository, times(1)).findCardByUserId(1);
        verify(cardMapper, times(1)).toDto(cardList);
    }

    @Test
    void testGetCardsByUserId_ReturnsEmptyList_WhenNoCardsFound() {
        when(cardRepository.findCardByUserId(1)).thenReturn(Collections.emptyList());
        when(cardMapper.toDto(Collections.emptyList())).thenReturn(Collections.emptyList());

        List<CardResponse> result = cardService.getCardsByUserId(1);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(cardRepository, times(1)).findCardByUserId(1);
        verify(cardMapper, times(1)).toDto(Collections.emptyList());
    }

    @Test
    void testDeleteCardByCardNumber_CardNotFound() {
        when(cardRepository.findCardByCardNumber(cardNumber)).thenReturn(null);

        assertThrows(EntityNotFoundException.class, () -> cardService.deleteCardByCardNumber(validEmail, cardNumber));

        verify(cardRepository, never()).deleteCardByCardNumber(cardNumber);
        verify(mailService, never()).sendMailForDeleteCard(any(), any());
    }

    @Test
    void testDeleteCardByCardNumber_UnauthorizedAccess() {
        when(cardRepository.findCardByCardNumber(cardNumber)).thenReturn(card1);

        assertThrows(UnauthorizedCardAccessException.class, () -> cardService.deleteCardByCardNumber(invalidEmail, cardNumber));

        verify(cardRepository, never()).deleteCardByCardNumber(cardNumber);
        verify(mailService, never()).sendMailForDeleteCard(any(), any());
    }

    @Test
    void testDeleteCardByCardNumber_SuccessfulDeletion() {
        String email = "user@example.com";
        String cardNumber = "1234567812345678";

        User user = new User();
        user.setEmail(email);

        Card cardInDb = new Card();
        cardInDb.setCardNumber(cardNumber);
        cardInDb.setUser(user);

        when(cardRepository.findCardByCardNumber(cardNumber)).thenReturn(cardInDb);

        cardService.deleteCardByCardNumber(email, cardNumber);

        verify(cardRepository, times(1)).deleteCardByCardNumber(cardNumber);

        verify(mailService, times(1)).sendMailForDeleteCard(email, cardNumber);
    }

    @Test
    void testGetCardByCardNumber_ReturnsCard_WhenCardExists() {
        when(cardRepository.findCardByCardNumber(cardNumber)).thenReturn(card1);

        Card result = cardService.getCardByCardNumber(cardNumber);

        assertNotNull(result);
        assertEquals(cardNumber, result.getCardNumber());

        verify(cardRepository, times(1)).findCardByCardNumber(cardNumber);
    }

    @Test
    void testGetCardByCardNumber_ReturnsNull_WhenCardDoesNotExist() {
        when(cardRepository.findCardByCardNumber(cardNumber)).thenReturn(null);

        Card result = cardService.getCardByCardNumber(cardNumber);

        assertNull(result);

        verify(cardRepository, times(1)).findCardByCardNumber(cardNumber);
    }

    // Test case for invalid input (e.g., null card number)
    @Test
    void testGetCardByCardNumber_ThrowsException_WhenCardNumberIsNull() {
        assertThrows(IllegalArgumentException.class, () -> cardService.getCardByCardNumber(null));

        verify(cardRepository, never()).findCardByCardNumber(anyString());
    }

    // Test case for invalid input (e.g., empty card number)
    @Test
    void testGetCardByCardNumber_ThrowsException_WhenCardNumberIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> cardService.getCardByCardNumber(""));

        verify(cardRepository, never()).findCardByCardNumber(anyString());
    }

    @Test
    void testEditCard_Success() {
        cardService.editCard(card1);

        verify(cardRepository, times(1)).save(card1);
    }

    @Test
    void testEditCard_NullCard() {
        assertThrows(IllegalArgumentException.class, () -> cardService.editCard(null));

        verify(cardRepository, never()).save(any(Card.class));
    }

    @Test
    void testEditCard_CardWithMissingFields() {
        card1.setCardNumber(null);  // For example, an invalid card number

        assertDoesNotThrow(() -> cardService.editCard(card1)); // No validation logic was added, so no exception should be thrown.

        verify(cardRepository, times(1)).save(card1);
    }

    @Test
    void testEditCard_VerifySaveCall() {
        cardService.editCard(card1);

        verify(cardRepository, times(1)).save(card1);
    }

    @Test
    void testGetCardByUserIdAndMainIs_ReturnsCard() {
        when(cardRepository.findCardByUserIdAndPrimary(1, true)).thenReturn(card1);

        Card result = cardService.getCardByUserIdAndMainIs(1, true);

        assertNotNull(result);
        assertEquals("1234567812345678", result.getCardNumber());
        assertTrue(result.isPrimary());

        verify(cardRepository, times(1)).findCardByUserIdAndPrimary(1, true);
    }

    @Test
    void testGetCardByUserIdAndMainIs_ReturnsNull_WhenNoCardFound() {
        when(cardRepository.findCardByUserIdAndPrimary(1, true)).thenReturn(null);

        Card result = cardService.getCardByUserIdAndMainIs(1, true);

        assertNull(result);

        verify(cardRepository, times(1)).findCardByUserIdAndPrimary(1, true);
    }

    @Test
    void testGetCardByUserIdAndMainIs_ReturnsNull_WhenCardIsNotPrimary() {
        Card nonPrimaryCard = new Card();
        nonPrimaryCard.setId(2);
        nonPrimaryCard.setCardNumber("8765432187654321");
        nonPrimaryCard.setExpirationDate(new Date());
        nonPrimaryCard.setCvvCode("456");
        nonPrimaryCard.setBalance(BigDecimal.ZERO);
        nonPrimaryCard.setPrimary(false); // Non-primary card

        when(cardRepository.findCardByUserIdAndPrimary(1, false)).thenReturn(nonPrimaryCard);

        Card result = cardService.getCardByUserIdAndMainIs(1, false);

        assertNotNull(result);
        assertEquals("8765432187654321", result.getCardNumber());
        assertFalse(result.isPrimary());

        verify(cardRepository, times(1)).findCardByUserIdAndPrimary(1, false);
    }

    @Test
    void testGetCardByUserIdAndMainIs_ThrowsException_WhenUserIdIsInvalid() {
        when(cardRepository.findCardByUserIdAndPrimary(999, true)).thenReturn(null);

        assertNull(cardService.getCardByUserIdAndMainIs(999, true));

        verify(cardRepository, times(1)).findCardByUserIdAndPrimary(999, true);
    }

    @Test
    void testSave_SuccessfullySavesCard() {
        when(cardRepository.save(any(Card.class))).thenReturn(card1);  // Mock save method to return card1

        cardService.save(card1);

        verify(cardRepository, times(1)).save(card1);
    }

    @Test
    void testSave_ThrowsException_WhenCardIsNull() {
        assertThrows(IllegalArgumentException.class, () -> cardService.save((Card) null));
    }

    @Test
    void testSave_ValidCard_SavesSuccessfully() {
        when(cardRepository.save(any(Card.class))).thenReturn(card1);  // Mock save to return card1

        cardService.save(card1);

        verify(cardRepository, times(1)).save(card1);
    }

    @Test
    void testSave_CallsRepositoryOnce() {
        when(cardRepository.save(any(Card.class))).thenReturn(card1);  // Mock the return value of save

        cardService.save(card1);

        verify(cardRepository, times(1)).save(card1);  // Verify save is called exactly once
    }

    @Test
    void testGetCardsCountByUserId_ReturnsCorrectCount() {
        when(cardRepository.countByUserId(userId)).thenReturn(3); // Simulate 3 cards for this user

        int result = cardService.getCardsCountByUserId(userId);

        assertEquals(3, result); // Expect 3 cards for the given user ID
        verify(cardRepository, times(1)).countByUserId(userId); // Ensure repository method was called once
    }

    @Test
    void testGetCardsCountByUserId_ReturnsZero_WhenNoCardsFound() {
        when(cardRepository.countByUserId(userId)).thenReturn(0); // Simulate no cards for this user

        int result = cardService.getCardsCountByUserId(userId);

        assertEquals(0, result); // Expect 0 cards for the given user ID
        verify(cardRepository, times(1)).countByUserId(userId); // Ensure repository method was called once
    }

    @Test
    void testGetCardsCountByUserId_ReturnsCorrectCount_WhenUserHasMultipleCards() {
        when(cardRepository.countByUserId(userId)).thenReturn(10); // Simulate 10 cards for this user

        int result = cardService.getCardsCountByUserId(userId);

        assertEquals(10, result); // Expect 10 cards for the given user ID
        verify(cardRepository, times(1)).countByUserId(userId); // Ensure repository method was called once
    }

    @Test
    void testSave_ShouldThrowCardCountException_WhenUserHasFourCards() {
        SaveCardRequestRest request = new SaveCardRequestRest();
        request.setUserId(1);
        request.setCardNumber("1234567890");

        when(cardRepository.countByUserId(1)).thenReturn(4);

        CardCountException exception = assertThrows(CardCountException.class, () -> cardService.save(request));

        assertEquals("Card count can't be more than 4", exception.getMessage());
    }

    @Test
    void testSave_ShouldThrowCardOnlyExistException_WhenCardExists() {
        SaveCardRequestRest request = new SaveCardRequestRest();
        request.setUserId(1);
        request.setCardNumber("1234567890");

        when(cardRepository.existsCardByCardNumber("1234567890")).thenReturn(true);

        CardOnlyExistException exception = assertThrows(CardOnlyExistException.class, () -> cardService.save(request));

        assertEquals("Card already exists", exception.getMessage());
    }

    @Test
    void testSave_ShouldSetPrimaryTrue_WhenNoCardsExistForUser() {
        SaveCardRequestRest request = new SaveCardRequestRest();
        request.setUserId(1);
        request.setCardNumber("1234567890");

        when(cardRepository.countByUserId(1)).thenReturn(0); // No cards for this user
        when(cardRepository.existsCardByCardNumber("1234567890")).thenReturn(false);

        Card card = new Card();
        when(cardMapper.toEntity(request)).thenReturn(card);

        when(cardRepository.findByUserId(1)).thenReturn(Optional.empty());

        cardService.save(request);

        assertTrue(card.isPrimary());
        verify(cardRepository, times(1)).save(card);
    }

    @Test
    void testSave_ShouldNotSetPrimary_WhenCardsExistForUser() {
        SaveCardRequestRest request = new SaveCardRequestRest();
        request.setUserId(1);
        request.setCardNumber("1234567890");

        when(cardRepository.countByUserId(1)).thenReturn(0); // No cards for this user
        when(cardRepository.existsCardByCardNumber("1234567890")).thenReturn(false);

        Card card = new Card();
        when(cardMapper.toEntity(request)).thenReturn(card);

        when(cardRepository.findByUserId(1)).thenReturn(Optional.of(new Card()));

        cardService.save(request);

        assertFalse(card.isPrimary());
        verify(cardRepository, times(1)).save(card);
    }

    @Test
    void testSave_ShouldSaveCardSuccessfully_WhenAllConditionsPass() {
        SaveCardRequestRest request = new SaveCardRequestRest();
        request.setUserId(1);
        request.setCardNumber("1234567890");

        when(cardRepository.countByUserId(1)).thenReturn(0); // No cards for the user
        when(cardRepository.existsCardByCardNumber("1234567890")).thenReturn(false);

        Card card = new Card();
        when(cardMapper.toEntity(request)).thenReturn(card);

        when(cardRepository.findByUserId(1)).thenReturn(Optional.empty());

        cardService.save(request);

        assertTrue(card.isPrimary());
        verify(cardRepository, times(1)).save(card);
    }
}










