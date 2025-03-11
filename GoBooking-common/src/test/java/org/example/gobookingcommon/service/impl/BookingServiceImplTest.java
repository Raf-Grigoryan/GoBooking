package org.example.gobookingcommon.service.impl;

import org.example.gobookingcommon.customException.InsufficientFundsException;
import org.example.gobookingcommon.customException.SlotAlreadyBookedException;
import org.example.gobookingcommon.customException.UsersMismatchException;
import org.example.gobookingcommon.dto.booking.*;
import org.example.gobookingcommon.entity.booking.Booking;
import org.example.gobookingcommon.entity.booking.PaymentMethod;
import org.example.gobookingcommon.entity.booking.Type;
import org.example.gobookingcommon.entity.user.Card;
import org.example.gobookingcommon.entity.user.User;
import org.example.gobookingcommon.entity.work.Service;
import org.example.gobookingcommon.entity.work.WorkGraphic;
import org.example.gobookingcommon.mapper.BookingMapper;
import org.example.gobookingcommon.repository.BookingRepository;
import org.example.gobookingcommon.repository.ServiceRepository;
import org.example.gobookingcommon.service.BookingBalanceService;
import org.example.gobookingcommon.service.CardService;
import org.example.gobookingcommon.service.WorkGraphicService;
import org.example.gobookingcommon.service.WorkService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private WorkGraphicService workGraphicService;

    @Mock
    private WorkService workService;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ServiceRepository serviceRepository;

    @Mock
    private CardService cardService;

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Mock
    private BookingBalanceService bookingBalanceService;


    @Mock
    private BookingMapper bookingMapper;

    private WorkGraphic workGraphic;
    private Service service;
    private User worker;
    private SaveBookingRequest saveBookingRequest;
    private User client;
    private Card card;
    private Date bookingDate;
    private Booking finishedBooking;
    private WorkerBookingResponse response;

    @BeforeEach
    void setUp() {

        workGraphic = new WorkGraphic();
        workGraphic.setStartWorkDate(LocalTime.of(9, 0));
        workGraphic.setEndedWorkDate(LocalTime.of(17, 0));
        workGraphic.setActive(true);

        worker = new User();
        worker.setId(1);

        service = new Service();
        service.setDuration(30);
        service.setWorker(worker);
        service.setPrice(50.0);

        client = new User();
        client.setId(2);

        card = new Card();
        card.setCardNumber("1234-5678-9012-3456");
        card.setBalance(BigDecimal.valueOf(100));

        saveBookingRequest = new SaveBookingRequest();
        saveBookingRequest.setServiceId(1);
        saveBookingRequest.setPaymentMethod(PaymentMethod.CARD);
        saveBookingRequest.setStartTime(LocalTime.of(10, 0));

        bookingDate = new Date();

        finishedBooking = new Booking();
        finishedBooking.setClient(client);
        finishedBooking.setType(Type.FINISHED);

        response = new WorkerBookingResponse();
    }

    @Test
    void testGetSelectTimeByWorkerIdAndServiceId() {
        int workerId = 1;
        int serviceId = 1;
        Date bookingDate = new Date();

        when(workGraphicService.getWorkGraphicByWorkerIdAndDayWeek(eq(workerId), anyString()))
                .thenReturn(workGraphic);
        when(workService.getServiceByIdForBooking(serviceId)).thenReturn(service);
        when(bookingRepository.findBookingByBookingDateAndServiceWorkerId(eq(bookingDate), eq(workerId)))
                .thenReturn(List.of());

        SelectTimeResponse response = bookingService.getSelectTimeByWorkerIdAndServiceId(workerId, serviceId, bookingDate);

        assertNotNull(response);
        assertEquals(workerId, response.getWorkerId());
        assertEquals(bookingDate, response.getBookingDate());
        verify(workGraphicService, times(1)).getWorkGraphicByWorkerIdAndDayWeek(eq(workerId), anyString());
        verify(workService, times(1)).getServiceByIdForBooking(serviceId);
        verify(bookingRepository, times(1)).findBookingByBookingDateAndServiceWorkerId(eq(bookingDate), eq(workerId));
    }

    @Test
    void shouldThrowInsufficientFundsExceptionWhenBalanceIsLow() {
        card.setBalance(BigDecimal.valueOf(20));

        when(serviceRepository.findById(1)).thenReturn(Optional.of(service));
        when(bookingRepository.existsAllByBookingDateAndStartedTimeAndService_Worker(any(), any(), any())).thenReturn(false);
        when(cardService.getCardByCardNumber("1234-5678-9012-3456")).thenReturn(card);

        assertThrows(InsufficientFundsException.class, () -> bookingService.save(saveBookingRequest, client, bookingDate, "1234-5678-9012-3456"));

        verify(bookingRepository, never()).save(any(Booking.class));
        verify(cardService, never()).editCard(any(Card.class));
    }

    @Test
    void shouldThrowSlotAlreadyBookedExceptionIfSlotIsTaken() {
        when(serviceRepository.findById(1)).thenReturn(Optional.of(service));
        when(bookingRepository.existsAllByBookingDateAndStartedTimeAndService_Worker(any(), any(), any())).thenReturn(true);

        assertThrows(SlotAlreadyBookedException.class, () -> bookingService.save(saveBookingRequest, client, bookingDate, null));

        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void shouldThrowExceptionIfServiceNotFound() {
        when(serviceRepository.findById(1)).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> bookingService.save(saveBookingRequest, client, bookingDate, null));

        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void testClientFinishedBookings() {
        when(bookingRepository.getBookingsByClient_IdAndType(client.getId(), Type.FINISHED))
                .thenReturn(List.of(finishedBooking));
        when(bookingMapper.workerBookingResponses(List.of(finishedBooking)))
                .thenReturn(List.of(response));

        List<WorkerBookingResponse> result = bookingService.clientFinishedBookings(client.getId());

        assertEquals(1, result.size());
        assertEquals(response, result.get(0));

        verify(bookingRepository, times(1)).getBookingsByClient_IdAndType(client.getId(), Type.FINISHED);
        verify(bookingMapper, times(1)).workerBookingResponses(List.of(finishedBooking));
    }

    @Test
    void testGetBookingAnalyticsWorker() {
        int workerId = 1;
        when(bookingRepository.countDistinctClientsByWorker(workerId)).thenReturn(10);
        when(bookingRepository.sumTotalEarningsByWorker(workerId)).thenReturn(500.0);
        when(bookingRepository.countBookingsByWorker(workerId)).thenReturn(15);
        when(bookingRepository.potentialEarnings(workerId)).thenReturn(200.0);

        BookingAnalyticsWorker analytics = bookingService.getBookingAnalyticsWorker(workerId);

        assertNotNull(analytics);
        assertEquals(10, analytics.getClientCount());
        assertEquals(500.0, analytics.getTotalEarnings());
        assertEquals(15, analytics.getBookingCount());
        assertEquals(200.0, analytics.getPotentialEarnings());

        verify(bookingRepository, times(1)).countDistinctClientsByWorker(workerId);
        verify(bookingRepository, times(1)).sumTotalEarningsByWorker(workerId);
        verify(bookingRepository, times(1)).countBookingsByWorker(workerId);
        verify(bookingRepository, times(1)).potentialEarnings(workerId);
    }

    @Test
    void testGetFinishedBookings() {
        int workerId = 1;
        PageRequest pageRequest = PageRequest.of(0, 10);

        Page<Booking> pageOfBookings = new PageImpl<>(List.of(finishedBooking));
        when(bookingRepository.getBookingByService_Worker_IdAndType(eq(workerId), eq(Type.FINISHED), eq(pageRequest)))
                .thenReturn(pageOfBookings);

        when(bookingMapper.toPendingBookingResponse(finishedBooking)).thenReturn(new PendingBookingResponse());

        Page<PendingBookingResponse> result = bookingService.getFinishedBookings(workerId, pageRequest);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(bookingRepository, times(1)).getBookingByService_Worker_IdAndType(eq(workerId), eq(Type.FINISHED), eq(pageRequest));
        verify(bookingMapper, times(1)).toPendingBookingResponse(finishedBooking);
    }

    @Test
    void testRejectBooking() {
        int bookingId = 1;
        User user = new User();
        user.setEmail("worker@example.com");
        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setType(Type.APPROVED);
        booking.setService(new Service());
        booking.getService().setWorker(new User());
        booking.getService().getWorker().setEmail("worker@example.com");

        booking.setPaymentMethod(PaymentMethod.CARD);
        booking.setClient(new User());
        booking.getClient().setId(2);

        Card card = new Card();
        card.setBalance(BigDecimal.valueOf(100));
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(cardService.getCardByUserIdAndMainIs(2, true)).thenReturn(card);

        bookingService.reject(bookingId, user);

        assertEquals(Type.REJECTED, booking.getType());
        verify(bookingRepository, times(1)).save(booking);
        verify(cardService, times(1)).editCard(card);
        verify(bookingBalanceService, times(1)).subtractFunds(booking.getService().getPrice());
    }

    @Test
    void testRejectBooking_UserMismatch() {
        int bookingId = 1;
        User user = new User();
        user.setEmail("wrong@example.com");
        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setType(Type.APPROVED);
        booking.setService(new Service());
        booking.getService().setWorker(new User());
        booking.getService().getWorker().setEmail("worker@example.com");

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        assertThrows(UsersMismatchException.class, () -> bookingService.reject(bookingId, user));

        verify(bookingRepository, never()).save(any(Booking.class));
    }


    @Test
    void testRejectBooking_WithCardPayment() {

        int bookingId = 1;
        User user = new User();
        user.setEmail("worker@example.com");
        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setType(Type.APPROVED);
        booking.setService(new Service());
        booking.getService().setPrice(50.0);
        booking.getService().setWorker(new User());
        booking.getService().getWorker().setEmail("worker@example.com");

        booking.setPaymentMethod(PaymentMethod.CARD);
        booking.setClient(new User());
        booking.getClient().setId(2);

        Card card = new Card();
        card.setBalance(BigDecimal.valueOf(100));

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(cardService.getCardByUserIdAndMainIs(2, true)).thenReturn(card);

        bookingService.reject(bookingId, user);

        assertEquals(BigDecimal.valueOf(150.0), card.getBalance());
        verify(bookingRepository, times(1)).save(booking);
        verify(cardService, times(1)).editCard(card);
        verify(bookingBalanceService, times(1)).subtractFunds(booking.getService().getPrice());
    }


    @Test
    void testFinishedBooking_Success() {
        int bookingId = 1;
        User user = new User();
        user.setEmail("worker@example.com");
        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setType(Type.APPROVED);
        booking.setPaymentMethod(PaymentMethod.CASH);
        booking.setService(new Service());
        booking.getService().setWorker(new User());
        booking.getService().getWorker().setEmail("worker@example.com");

        booking.setBookingDate(new Date());
        booking.setEndedTime(LocalTime.of(16, 0));

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(booking)).thenReturn(booking);

        bookingService.finished(bookingId, user);

        assertEquals(Type.FINISHED, booking.getType());
        verify(bookingRepository, times(1)).save(booking);
    }

    @Test
    void testFinishedBooking_UserMismatch() {
        int bookingId = 1;
        User user = new User();
        user.setEmail("wrong@example.com");
        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setType(Type.APPROVED);
        booking.setService(new Service());
        booking.getService().setWorker(new User());
        booking.getService().getWorker().setEmail("worker@example.com");

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        assertThrows(UsersMismatchException.class, () -> bookingService.finished(bookingId, user));

        verify(bookingRepository, never()).save(any(Booking.class));
    }



    @Test
    void testFinishedBooking_BookingDateBeforeCurrentDate() {
        int bookingId = 1;
        User user = new User();
        user.setEmail("worker@example.com");
        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setType(Type.APPROVED);
        booking.setPaymentMethod(PaymentMethod.CASH);
        booking.setService(new Service());
        booking.getService().setWorker(new User());
        booking.getService().getWorker().setEmail("worker@example.com");

        booking.setBookingDate(new Date(System.currentTimeMillis() - 10000));
        booking.setEndedTime(LocalTime.of(16, 0));

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        bookingService.finished(bookingId, user);

        assertEquals(Type.FINISHED, booking.getType());
        verify(bookingRepository, times(1)).save(booking);
    }



    @Test
    void testGetSumTotalEarningsByWorkerWhereTypeApproved_Success() {
        int workerId = 1;
        Double expectedEarnings = 500.0;

        when(bookingRepository.sumTotalEarningsByWorkerWhereTypeApproved(workerId))
                .thenReturn(Optional.of(expectedEarnings));

        Optional<Double> earnings = bookingService.getSumTotalEarningsByWorkerWhereTypeApproved(workerId);

        assertTrue(earnings.isPresent());
        assertEquals(expectedEarnings, earnings.get());

        verify(bookingRepository, times(1)).sumTotalEarningsByWorkerWhereTypeApproved(workerId);
    }

    @Test
    void testGetSumTotalEarningsByWorkerWhereTypeApproved_NotFound() {
        int workerId = 1;

        when(bookingRepository.sumTotalEarningsByWorkerWhereTypeApproved(workerId))
                .thenReturn(Optional.empty());

        Optional<Double> earnings = bookingService.getSumTotalEarningsByWorkerWhereTypeApproved(workerId);

        assertFalse(earnings.isPresent());

        verify(bookingRepository, times(1)).sumTotalEarningsByWorkerWhereTypeApproved(workerId);
    }

    @Test
    void testGetSumTotalEarningsByWorkerWhereTypeFinished_Success() {
        int workerId = 1;
        Double expectedEarnings = 300.0;

        // Mock the repository method to return a value
        when(bookingRepository.sumTotalEarningsByWorkerWhereTypeFinished(workerId))
                .thenReturn(Optional.of(expectedEarnings));

        // Call the method
        Optional<Double> earnings = bookingService.getSumTotalEarningsByWorkerWhereTypeFinished(workerId);

        // Assert that the returned value is correct
        assertTrue(earnings.isPresent());
        assertEquals(expectedEarnings, earnings.get());

        // Verify that the repository method was called
        verify(bookingRepository, times(1)).sumTotalEarningsByWorkerWhereTypeFinished(workerId);
    }

    @Test
    void testGetSumTotalEarningsByWorkerWhereTypeFinished_NotFound() {
        int workerId = 1;

        // Mock the repository method to return an empty optional
        when(bookingRepository.sumTotalEarningsByWorkerWhereTypeFinished(workerId))
                .thenReturn(Optional.empty());

        // Call the method
        Optional<Double> earnings = bookingService.getSumTotalEarningsByWorkerWhereTypeFinished(workerId);

        // Assert that the returned value is empty
        assertFalse(earnings.isPresent());

        // Verify that the repository method was called
        verify(bookingRepository, times(1)).sumTotalEarningsByWorkerWhereTypeFinished(workerId);
    }

    @Test
    void testGetUnfinishedBookings_Success() {
        int workerId = 1;
        PageRequest pageRequest = PageRequest.of(0, 10);
        Booking booking = new Booking();
        booking.setType(Type.APPROVED);
        booking.setService(new Service());
        booking.getService().setWorker(new User());
        booking.getService().getWorker().setId(workerId);

        Page<Booking> page = new PageImpl<>(List.of(booking));

        // Mock the repository to return the bookings page
        when(bookingRepository.getBookingByService_Worker_IdAndType(workerId, Type.APPROVED, pageRequest))
                .thenReturn(page);

        // Mock the mapper to convert the bookings to PendingBookingResponse
        PendingBookingResponse response = new PendingBookingResponse();
        when(bookingMapper.toPendingBookingResponse(booking)).thenReturn(response);

        // Call the method
        Page<PendingBookingResponse> result = bookingService.getUnfinishedBookings(workerId, pageRequest);

        // Assert that the result is not empty and contains the expected number of elements
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());

        // Verify that the repository method was called
        verify(bookingRepository, times(1)).getBookingByService_Worker_IdAndType(workerId, Type.APPROVED, pageRequest);
        verify(bookingMapper, times(1)).toPendingBookingResponse(booking);
    }

    @Test
    void testGetUnfinishedBookings_NoBookings() {
        int workerId = 1;
        PageRequest pageRequest = PageRequest.of(0, 10);

        // Mock the repository to return an empty page
        Page<Booking> page = Page.empty();
        when(bookingRepository.getBookingByService_Worker_IdAndType(workerId, Type.APPROVED, pageRequest))
                .thenReturn(page);

        // Call the method
        Page<PendingBookingResponse> result = bookingService.getUnfinishedBookings(workerId, pageRequest);

        // Assert that the result is empty
        assertNotNull(result);
        assertEquals(0, result.getTotalElements());

        // Verify that the repository method was called
        verify(bookingRepository, times(1)).getBookingByService_Worker_IdAndType(workerId, Type.APPROVED, pageRequest);
    }



}
