package org.example.gobookingcommon.service.impl;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.example.gobookingcommon.entity.bookingBalance.BookingBalance;
import org.example.gobookingcommon.repository.BookingBalanceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.math.BigDecimal;
import java.util.Optional;

class BookingBalanceServiceTest {

    @Mock
    private BookingBalanceRepository bookingBalanceRepository;

    @InjectMocks
    private BookingBalanceServiceImpl bookingBalanceService;

    private static final int BALANCE_ID = 1;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        bookingBalanceService = new BookingBalanceServiceImpl(bookingBalanceRepository);
    }

    @Test
    void shouldAddFundsWhenBookingBalanceDoesNotExist() {

        double money = 500.00;
        when(bookingBalanceRepository.findById(BookingBalanceServiceImpl.BALANCE_ID)).thenReturn(Optional.empty());

        bookingBalanceService.addFunds(money);


        verify(bookingBalanceRepository, times(1)).save(any(BookingBalance.class));
    }

    @Test
    void shouldAddFundsWhenBookingBalanceExists() {
        double money = 500.00;
        BookingBalance existingBalance = new BookingBalance();
        existingBalance.setBalance(BigDecimal.valueOf(200.00));
        when(bookingBalanceRepository.findById(BookingBalanceServiceImpl.BALANCE_ID)).thenReturn(Optional.of(existingBalance));

        bookingBalanceService.addFunds(money);

        verify(bookingBalanceRepository, times(1)).save(existingBalance);
        assertEquals(BigDecimal.valueOf(700.00), existingBalance.getBalance());
    }

    @Test
    void shouldHandleExceptionWhenSavingBookingBalance() {

        double money = 500.00;
        when(bookingBalanceRepository.findById(BookingBalanceServiceImpl.BALANCE_ID)).thenReturn(Optional.empty());
        doThrow(new RuntimeException("Database error")).when(bookingBalanceRepository).save(any(BookingBalance.class));

        assertThrows(RuntimeException.class, () -> bookingBalanceService.addFunds(money));
    }

    @Test
    void shouldNotAddFundsWhenBalanceIsZero() {
        double money = 0.0;
        BookingBalance existingBalance = new BookingBalance();
        existingBalance.setBalance(BigDecimal.valueOf(200.00));
        when(bookingBalanceRepository.findById(BookingBalanceServiceImpl.BALANCE_ID)).thenReturn(Optional.of(existingBalance));

        bookingBalanceService.addFunds(money);

        verify(bookingBalanceRepository, times(0)).save(existingBalance);
        assertEquals(BigDecimal.valueOf(200.00), existingBalance.getBalance());
    }

    @Test
    void shouldSubtractFundsSuccessfully() {
        // Given
        double moneyToSubtract = 50.0;
        BookingBalance existingBalance = new BookingBalance();
        existingBalance.setBalance(BigDecimal.valueOf(200.00));
        when(bookingBalanceRepository.findById(BookingBalanceServiceImpl.BALANCE_ID))
                .thenReturn(Optional.of(existingBalance));

        bookingBalanceService.subtractFunds(moneyToSubtract);

        BigDecimal expectedBalance = BigDecimal.valueOf(150.00);
        assertEquals(expectedBalance, existingBalance.getBalance());
        verify(bookingBalanceRepository, times(1)).save(existingBalance); // Ensure save was called
    }

    @Test
    void shouldNotSubtractFundsWhenBalanceIsZero() {

        double moneyToSubtract = 0.0;
        BookingBalance existingBalance = new BookingBalance();
        existingBalance.setBalance(BigDecimal.valueOf(200.00));
        when(bookingBalanceRepository.findById(BookingBalanceServiceImpl.BALANCE_ID))
                .thenReturn(Optional.of(existingBalance));

        bookingBalanceService.subtractFunds(moneyToSubtract);

        assertEquals(BigDecimal.valueOf(200.00), existingBalance.getBalance()); // Balance should remain unchanged
    }

    @Test
    void shouldNotSubtractFundsWhenBalanceDoesNotExist() {
        double moneyToSubtract = 50.0;
        when(bookingBalanceRepository.findById(BookingBalanceServiceImpl.BALANCE_ID))
                .thenReturn(Optional.empty());

        bookingBalanceService.subtractFunds(moneyToSubtract);

        verify(bookingBalanceRepository, times(0)).save(any(BookingBalance.class));
    }

    @Test
    void shouldNotAllowNegativeBalanceWhenSubtractingFunds() {
        double moneyToSubtract = 250.0;
        BookingBalance existingBalance = new BookingBalance();
        existingBalance.setBalance(BigDecimal.valueOf(200.00));
        when(bookingBalanceRepository.findById(BookingBalanceServiceImpl.BALANCE_ID))
                .thenReturn(Optional.of(existingBalance));

        bookingBalanceService.subtractFunds(moneyToSubtract);

        assertEquals(BigDecimal.valueOf(200.00), existingBalance.getBalance());
        verify(bookingBalanceRepository, times(0)).save(existingBalance); // Ensure save was NOT called
    }

    @Test
    void shouldReturnBookingBalanceWhenExists() {
        BookingBalance bookingBalance = new BookingBalance();
        bookingBalance.setBalance(BigDecimal.valueOf(100.0));
        when(bookingBalanceRepository.findById(BALANCE_ID)).thenReturn(Optional.of(bookingBalance));

        double balance = bookingBalanceService.getBookingBalance();

        assertEquals(100.0, balance);
    }

    @Test
    void shouldReturnZeroWhenBalanceDoesNotExist() {
        when(bookingBalanceRepository.findById(BALANCE_ID)).thenReturn(Optional.empty());

        double balance = bookingBalanceService.getBookingBalance();

        assertEquals(0.0, balance);
    }

}