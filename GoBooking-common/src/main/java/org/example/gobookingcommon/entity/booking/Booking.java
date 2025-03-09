package org.example.gobookingcommon.entity.booking;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.gobookingcommon.entity.user.User;
import org.example.gobookingcommon.entity.work.Service;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;
import java.util.Date;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    private Service service;
    @ManyToOne
    private User client;
    private LocalTime startedTime;
    private LocalTime endedTime;
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    @Enumerated(EnumType.STRING)
    private Type type;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date bookingDate;
}
