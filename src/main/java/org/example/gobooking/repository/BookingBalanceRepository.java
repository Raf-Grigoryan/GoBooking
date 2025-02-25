package org.example.gobooking.repository;

import org.example.gobooking.entity.bookingBalance.BookingBalance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingBalanceRepository extends JpaRepository<BookingBalance, Integer> {
}
