package org.example.gobooking.entity.user;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String cardNumber;
    @DateTimeFormat(pattern = "MM/YY")
    private Date expirationDate;
    private String cvvCode;
    private BigDecimal balance;
    @ManyToOne
    private User user;
}
