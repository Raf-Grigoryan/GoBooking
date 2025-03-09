package org.example.gobookingcommon.service.impl;

import lombok.RequiredArgsConstructor;

import org.example.gobookingcommon.entity.bookingBalance.BookingBalance;
import org.example.gobookingcommon.repository.BookingBalanceRepository;
import org.example.gobookingcommon.service.BookingBalanceService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingBalanceServiceImpl implements BookingBalanceService {


    static final int BALANCE_ID = 1;

    private final BookingBalanceRepository bookingBalanceRepository;


    @Override
    public synchronized void addFunds(double money) {
        Optional<BookingBalance> bookingBalanceOpt = bookingBalanceRepository.findById(BALANCE_ID);
        if (bookingBalanceOpt.isEmpty()) {
            BookingBalance bookingBalance = new BookingBalance();
            bookingBalance.setBalance(BigDecimal.valueOf(money));
            bookingBalanceRepository.save(bookingBalance);
        } else {
            BookingBalance existingBalance = bookingBalanceOpt.get();
            BigDecimal newBalance = existingBalance.getBalance().add(BigDecimal.valueOf(money));

            if (money != 0.0) {
                existingBalance.setBalance(newBalance);
                bookingBalanceRepository.save(existingBalance);
            }
        }
    }

    @Override
    public synchronized void subtractFunds(double money) {
        Optional<BookingBalance> bookingBalanceOpt = bookingBalanceRepository.findById(BALANCE_ID);
        if (bookingBalanceOpt.isPresent()) {
            BigDecimal currentBalance = bookingBalanceOpt.get().getBalance();
            BigDecimal newBalance = currentBalance.subtract(BigDecimal.valueOf(money));
            if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
                return;
            }
            bookingBalanceOpt.get().setBalance(newBalance);
            bookingBalanceRepository.save(bookingBalanceOpt.get());
        }
    }


    @Override
    public double getBookingBalance() {
        Optional<BookingBalance> bookingBalanceOpt = bookingBalanceRepository.findById(BALANCE_ID);
        return bookingBalanceOpt.map(bookingBalance -> bookingBalance.getBalance().doubleValue()).orElse(0.0);
    }


}
