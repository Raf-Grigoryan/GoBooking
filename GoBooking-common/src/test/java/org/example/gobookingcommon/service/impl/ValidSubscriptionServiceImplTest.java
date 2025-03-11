package org.example.gobookingcommon.service.impl;

import org.example.gobookingcommon.customException.CardNotExistException;
import org.example.gobookingcommon.customException.InsufficientFundsException;
import org.example.gobookingcommon.entity.company.Company;
import org.example.gobookingcommon.entity.subscription.Subscription;
import org.example.gobookingcommon.entity.subscription.ValidSubscription;

import org.example.gobookingcommon.entity.user.Card;
import org.example.gobookingcommon.entity.user.User;
import org.example.gobookingcommon.repository.ValidSubscriptionRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class ValidSubscriptionServiceTest {

    @Mock
    private SubscriptionServiceImpl subscriptionService;
    @Mock
    private CardServiceImpl cardService;
    @Mock
    private MailServiceImpl mailService;
    @Mock
    private ProjectFinanceServiceImpl projectFinanceService;
    @Mock
    private ValidSubscriptionRepository validSubscriptionRepository;
    @InjectMocks
    private ValidSubscriptionServiceImpl validSubscriptionService;
    @Mock
    private Company company;
    @Mock
    private Subscription subscription;
    @Mock
    private Card card;

    private static final String SUBSCRIPTION_TITLE = "Test Subscription";
    private static final String CARD_NUMBER = "1234-5678-9101-1121";


    @Test
    void shouldSaveValidSubscriptionWhenCardHasEnoughBalance() {
        when(subscriptionService.getSubscriptionByTitle(SUBSCRIPTION_TITLE)).thenReturn(subscription);
        when(cardService.getCardByCardNumber(CARD_NUMBER)).thenReturn(card);
        when(card.getBalance()).thenReturn(BigDecimal.valueOf(100));
        when(subscription.getPrice()).thenReturn(BigDecimal.valueOf(50));
        when(subscription.getDuration()).thenReturn(12);


        validSubscriptionService.save(company, SUBSCRIPTION_TITLE, CARD_NUMBER);

        verify(cardService).save(card);
        verify(validSubscriptionRepository).save(any(ValidSubscription.class));
        verify(projectFinanceService).addFunds(BigDecimal.valueOf(50));

        verify(card).setBalance(BigDecimal.valueOf(50));
    }

    @Test
    void shouldThrowCardNotExistExceptionWhenCardDoesNotExist() {
        when(cardService.getCardByCardNumber(CARD_NUMBER)).thenReturn(null);

        assertThrows(CardNotExistException.class, () ->
                validSubscriptionService.save(company, SUBSCRIPTION_TITLE, CARD_NUMBER)
        );
    }

    @Test
    void shouldThrowInsufficientFundsExceptionWhenCardHasInsufficientFunds() {
        when(subscriptionService.getSubscriptionByTitle(SUBSCRIPTION_TITLE)).thenReturn(subscription);
        when(cardService.getCardByCardNumber(CARD_NUMBER)).thenReturn(card);
        when(card.getBalance()).thenReturn(BigDecimal.valueOf(30));
        when(subscription.getPrice()).thenReturn(BigDecimal.valueOf(50));

        assertThrows(InsufficientFundsException.class, () ->
                validSubscriptionService.save(company, SUBSCRIPTION_TITLE, CARD_NUMBER)
        );
    }

    @Test
    void shouldCalculateCorrectSubscriptionDates() {
        when(subscriptionService.getSubscriptionByTitle(SUBSCRIPTION_TITLE)).thenReturn(subscription);
        when(cardService.getCardByCardNumber(CARD_NUMBER)).thenReturn(card);
        when(card.getBalance()).thenReturn(BigDecimal.valueOf(100));
        when(subscription.getPrice()).thenReturn(BigDecimal.valueOf(50));
        when(subscription.getDuration()).thenReturn(12);

        validSubscriptionService.save(company, SUBSCRIPTION_TITLE, CARD_NUMBER);

        ArgumentCaptor<ValidSubscription> validSubscriptionCaptor = ArgumentCaptor.forClass(ValidSubscription.class);
        verify(validSubscriptionRepository).save(validSubscriptionCaptor.capture());

        ValidSubscription validSubscription = validSubscriptionCaptor.getValue();
        Date startDate = validSubscription.getStartedDate();
        Date endDate = validSubscription.getEndedDate();

        Calendar expectedEndDate = Calendar.getInstance();
        expectedEndDate.setTime(startDate);
        expectedEndDate.add(Calendar.MONTH, 12);
        assertEquals(expectedEndDate.getTime(), endDate);
    }

    @Test
    void shouldSaveValidSubscriptionAndNotifyUser() {
        when(subscriptionService.getSubscriptionByTitle(SUBSCRIPTION_TITLE)).thenReturn(subscription);
        when(cardService.getCardByCardNumber(CARD_NUMBER)).thenReturn(card);
        when(card.getBalance()).thenReturn(BigDecimal.valueOf(100));
        when(subscription.getPrice()).thenReturn(BigDecimal.valueOf(50));


        validSubscriptionService.save(company, SUBSCRIPTION_TITLE, CARD_NUMBER);
    }

    @Test
    void shouldSendEmailsWhenSubscriptionsAreExpiringSoon() {
        ValidSubscription validSubscription1 = mock(ValidSubscription.class);
        ValidSubscription validSubscription2 = mock(ValidSubscription.class);

        Company company1 = mock(Company.class);
        User director1 = mock(User.class);
        when(validSubscription1.getCompany()).thenReturn(company1);
        when(company1.getDirector()).thenReturn(director1);
        when(director1.getEmail()).thenReturn("director1@example.com");
        when(director1.getName()).thenReturn("Director 1");

        Company company2 = mock(Company.class);
        User director2 = mock(User.class);
        when(validSubscription2.getCompany()).thenReturn(company2);
        when(company2.getDirector()).thenReturn(director2);
        when(director2.getEmail()).thenReturn("director2@example.com");
        when(director2.getName()).thenReturn("Director 2");
        when(validSubscription1.getEndedDate()).thenReturn(new Date(System.currentTimeMillis() + (2L * 24 * 60 * 60 * 1000))); // 2 days from now
        when(validSubscription2.getEndedDate()).thenReturn(new Date(System.currentTimeMillis() + ((long) 24 * 60 * 60 * 1000))); // 1 day from now

        when(validSubscriptionRepository.findByEndedDate(any(Date.class)))
                .thenReturn(Arrays.asList(validSubscription1, validSubscription2));

        validSubscriptionService.notifyExpiringSubscriptions();

        verify(mailService, times(2)).sendSubscriptionExpiryEmail(anyString(), anyString(), anyString());

        // Capture arguments passed to the send method to check email content
        ArgumentCaptor<String> emailCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> nameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> dateCaptor = ArgumentCaptor.forClass(String.class);

        // Verify the values passed to the sendSubscriptionExpiryEmail method
        verify(mailService, times(2)).sendSubscriptionExpiryEmail(emailCaptor.capture(), nameCaptor.capture(), dateCaptor.capture());

        assertEquals("director1@example.com", emailCaptor.getAllValues().get(0));
        assertEquals("Director 1", nameCaptor.getAllValues().get(0));
        assertEquals(validSubscription1.getEndedDate().toString(), dateCaptor.getAllValues().get(0));

        assertEquals("director2@example.com", emailCaptor.getAllValues().get(1));
        assertEquals("Director 2", nameCaptor.getAllValues().get(1));
        assertEquals(validSubscription2.getEndedDate().toString(), dateCaptor.getAllValues().get(1));
    }



    @Test
    void shouldNotSendEmailsWhenNoSubscriptionsAreExpiringSoon() {
        when(validSubscriptionRepository.findByEndedDate(any(Date.class)))
                .thenReturn(List.of()); // No expiring subscriptions

        validSubscriptionService.notifyExpiringSubscriptions();

        verify(mailService, never()).sendSubscriptionExpiryEmail(anyString(), anyString(), anyString());
    }

    @Test
    void shouldDeleteExpiredSubscriptionsAndSendEmails() {
        ValidSubscription expiredSubscription1 = mock(ValidSubscription.class);
        ValidSubscription expiredSubscription2 = mock(ValidSubscription.class);
        Company company1 = mock(Company.class);
        Company company2 = mock(Company.class);
        User director1 = mock(User.class);
        User director2 = mock(User.class);

        when(expiredSubscription1.getCompany()).thenReturn(company1);
        when(company1.getDirector()).thenReturn(director1);
        when(director1.getEmail()).thenReturn("director1@example.com");
        when(director1.getName()).thenReturn("Director 1");

        when(expiredSubscription2.getCompany()).thenReturn(company2);
        when(company2.getDirector()).thenReturn(director2);
        when(director2.getEmail()).thenReturn("director2@example.com");
        when(director2.getName()).thenReturn("Director 2");

        List<ValidSubscription> expiredSubscriptions = new ArrayList<>();
        expiredSubscriptions.add(expiredSubscription1);
        expiredSubscriptions.add(expiredSubscription2);

        when(validSubscriptionRepository.findByEndedDateBefore(any(Date.class)))
                .thenReturn(expiredSubscriptions);

        validSubscriptionService.deleteExpiredSubscriptions();

        verify(mailService, times(1)).sendSubscriptionDeletedEmail("director1@example.com", "Director 1");
        verify(mailService, times(1)).sendSubscriptionDeletedEmail("director2@example.com", "Director 2");
        verify(validSubscriptionRepository, times(1)).deleteAll(expiredSubscriptions);
    }

    @Test
    void shouldNotSendEmailWhenNoExpiredSubscriptions() {
        List<ValidSubscription> expiredSubscriptions = new ArrayList<>();
        when(validSubscriptionRepository.findByEndedDateBefore(any(Date.class)))
                .thenReturn(expiredSubscriptions);

        validSubscriptionService.deleteExpiredSubscriptions();

        verify(mailService, never()).sendSubscriptionDeletedEmail(anyString(), anyString());
        verify(validSubscriptionRepository, never()).deleteAll(anyList());
    }

    @Test
    void shouldHandleExceptionWhenSendingEmail() {
        ValidSubscription expiredSubscription = mock(ValidSubscription.class);
        Company company = mock(Company.class);
        User director = mock(User.class);

        when(expiredSubscription.getCompany()).thenReturn(company);
        when(company.getDirector()).thenReturn(director);
        when(director.getEmail()).thenReturn("director@example.com");
        when(director.getName()).thenReturn("Director");

        List<ValidSubscription> expiredSubscriptions = new ArrayList<>();
        expiredSubscriptions.add(expiredSubscription);

        when(validSubscriptionRepository.findByEndedDateBefore(any(Date.class)))
                .thenReturn(expiredSubscriptions);

        doThrow(new RuntimeException("Email service failure")).when(mailService)
                .sendSubscriptionDeletedEmail(anyString(), anyString());

        assertDoesNotThrow(() -> validSubscriptionService.deleteExpiredSubscriptions());

        verify(mailService, times(1)).sendSubscriptionDeletedEmail("director@example.com", "Director");
        verify(validSubscriptionRepository, times(1)).deleteAll(expiredSubscriptions);
    }


}