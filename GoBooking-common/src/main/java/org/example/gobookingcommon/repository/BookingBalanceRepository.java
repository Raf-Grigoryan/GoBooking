package org.example.gobookingcommon.repository;


import org.example.gobookingcommon.entity.bookingBalance.BookingBalance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingBalanceRepository extends JpaRepository<BookingBalance, Integer> {
}
