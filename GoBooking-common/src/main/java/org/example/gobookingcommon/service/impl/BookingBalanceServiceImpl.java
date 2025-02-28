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


    private final static int BALANCE_ID = 1;

    private final BookingBalanceRepository bookingBalanceRepository;


    @Override
    public synchronized void addFunds(double money) {
            Optional<BookingBalance> bookingBalanceOpt = bookingBalanceRepository.findById(BALANCE_ID);
            if (bookingBalanceOpt.isEmpty()) {
                BookingBalance bookingBalance = new BookingBalance();
                bookingBalance.setBalance(BigDecimal.valueOf(money));
                bookingBalanceRepository.save(bookingBalance);
            } else {
                bookingBalanceOpt.get().sum(BigDecimal.valueOf(money));
                bookingBalanceRepository.save(bookingBalanceOpt.get());
            }

    }

    @Override
    public synchronized void subtractFunds(double money) {
        Optional<BookingBalance> bookingBalanceOpt = bookingBalanceRepository.findById(BALANCE_ID);
        if (bookingBalanceOpt.isPresent()) {
            bookingBalanceOpt.get().subtract(BigDecimal.valueOf(money));
            bookingBalanceRepository.save(bookingBalanceOpt.get());
        }
    }

    @Override
    public double getBookingBalance() {
        Optional<BookingBalance> bookingBalanceOpt = bookingBalanceRepository.findById(BALANCE_ID);
        return bookingBalanceOpt.map(bookingBalance -> bookingBalance.getBalance().doubleValue()).orElse(0.0);
    }


}
