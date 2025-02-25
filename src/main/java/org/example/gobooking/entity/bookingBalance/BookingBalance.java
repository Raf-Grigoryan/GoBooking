package org.example.gobooking.entity.bookingBalance;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Entity
public class BookingBalance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    BigDecimal balance;

    public void sum(BigDecimal bookingBalance) {
        this.balance = this.balance.add(bookingBalance);
    }

    public void subtract(BigDecimal bookingBalance) {
        this.balance = this.balance.subtract(bookingBalance);
    }
}
