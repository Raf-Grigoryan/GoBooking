package org.example.gobookingcommon.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.example.gobookingcommon.customException.SubscriptionOnlyExistException;
import org.example.gobookingcommon.dto.subscription.SaveSubscriptionRequest;
import org.example.gobookingcommon.dto.subscription.SubscriptionDto;
import org.example.gobookingcommon.entity.subscription.Subscription;
import org.example.gobookingcommon.mapper.SubscriptionMapper;
import org.example.gobookingcommon.repository.SubscriptionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubscriptionServiceTest {

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @Mock
    private SubscriptionMapper subscriptionMapper;

    @InjectMocks
    private SubscriptionServiceImpl subscriptionService;

    private SaveSubscriptionRequest saveSubscriptionRequest;

    private Subscription subscription1;
    private Subscription subscription2;
    private SubscriptionDto subscriptionDto1;
    private SubscriptionDto subscriptionDto2;

    @BeforeEach
    void setUp() {
        saveSubscriptionRequest = new SaveSubscriptionRequest();
        saveSubscriptionRequest.setTitle("Test Subscription");

        subscription1 = new Subscription();
        subscription2 = new Subscription();

        subscriptionDto1 = new SubscriptionDto();
        subscriptionDto2 = new SubscriptionDto();
    }

    @Test
    void shouldSaveSubscriptionWhenTitleIsUnique() {

        when(subscriptionRepository.existsSubscriptionByTitle(saveSubscriptionRequest.getTitle())).thenReturn(false);
        when(subscriptionMapper.toEntity(saveSubscriptionRequest)).thenReturn(new Subscription());

        subscriptionService.save(saveSubscriptionRequest);

        verify(subscriptionRepository, times(1)).existsSubscriptionByTitle(saveSubscriptionRequest.getTitle());

        verify(subscriptionRepository, times(1)).save(any(Subscription.class));
    }

    @Test
    void shouldThrowExceptionWhenSubscriptionAlreadyExists() {

        when(subscriptionRepository.existsSubscriptionByTitle(saveSubscriptionRequest.getTitle())).thenReturn(true);

        SubscriptionOnlyExistException exception = assertThrows(SubscriptionOnlyExistException.class, () ->
                subscriptionService.save(saveSubscriptionRequest)
        );

        assertEquals("Subscription only exist", exception.getMessage());
    }

    @Test
    void shouldReturnListOfSubscriptionsWhenExist() {
        when(subscriptionRepository.findAll()).thenReturn(Arrays.asList(subscription1, subscription2));
        when(subscriptionMapper.toDtoList(Arrays.asList(subscription1, subscription2)))
                .thenReturn(Arrays.asList(subscriptionDto1, subscriptionDto2));

        List<SubscriptionDto> result = subscriptionService.getAllSubscriptions();


        assertEquals(2, result.size());
        assertTrue(result.contains(subscriptionDto1));
        assertTrue(result.contains(subscriptionDto2));


        verify(subscriptionRepository, times(1)).findAll();

        verify(subscriptionMapper, times(1)).toDtoList(Arrays.asList(subscription1, subscription2));
    }

    @Test
    void shouldReturnEmptyListWhenNoSubscriptions() {

        when(subscriptionRepository.findAll()).thenReturn(Collections.emptyList());


        List<SubscriptionDto> result = subscriptionService.getAllSubscriptions();

        assertTrue(result.isEmpty());

        verify(subscriptionRepository, times(1)).findAll();
    }

    @Test
    void shouldHandleExceptionWhenRepositoryFails() {
        when(subscriptionRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                subscriptionService.getAllSubscriptions()
        );

        assertEquals("Database error", exception.getMessage());

        verify(subscriptionRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnSubscriptionDtoWhenSubscriptionExists() {
        when(subscriptionRepository.findSubscriptionByTitle("Test Subscription")).thenReturn(subscription1);
        when(subscriptionMapper.toDto(subscription1)).thenReturn(subscriptionDto1);

        SubscriptionDto result = subscriptionService.getSubscriptionDtoByTitle("Test Subscription");

        assertEquals(subscriptionDto1, result);

        verify(subscriptionRepository, times(1)).findSubscriptionByTitle("Test Subscription");
        verify(subscriptionMapper, times(1)).toDto(subscription1);
    }

    @Test
    void shouldThrowEntityNotFoundExceptionWhenSubscriptionDoesNotExist() {
        when(subscriptionRepository.findSubscriptionByTitle("Non-existing Subscription")).thenReturn(null);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                subscriptionService.getSubscriptionDtoByTitle("Non-existing Subscription")
        );

        assertEquals("Subscription not found", exception.getMessage());

        verify(subscriptionRepository, times(1)).findSubscriptionByTitle("Non-existing Subscription");
    }

    @Test
    void shouldReturnSubscriptionWhenFoundByTitle() {
        Subscription mockSubscription = new Subscription();
        mockSubscription.setTitle("Test Subscription");
        when(subscriptionRepository.findSubscriptionByTitle("Test Subscription")).thenReturn(mockSubscription);

        Subscription result = subscriptionService.getSubscriptionByTitle("Test Subscription");

        assertNotNull(result);
        assertEquals("Test Subscription", result.getTitle());

        verify(subscriptionRepository, times(1)).findSubscriptionByTitle("Test Subscription");
    }

    @Test
    void shouldThrowEntityNotFoundExceptionWhenSubscriptionNotFound() {
        when(subscriptionRepository.findSubscriptionByTitle("Non-existing Subscription")).thenReturn(null);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                subscriptionService.getSubscriptionByTitle("Non-existing Subscription")
        );

        assertEquals("Subscription not found", exception.getMessage());

        verify(subscriptionRepository, times(1)).findSubscriptionByTitle("Non-existing Subscription");
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenTitleIsEmpty() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                subscriptionService.getSubscriptionByTitle("")
        );

        assertEquals("Subscription title cannot be empty", exception.getMessage());
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenTitleIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                subscriptionService.getSubscriptionByTitle(null)
        );

        assertEquals("Subscription title cannot be null", exception.getMessage());
    }



}
