package org.example.gobooking.entity.booking;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.gobooking.entity.user.User;
import org.example.gobooking.entity.work.Service;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;
import java.util.Date;

@Data
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
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date bookingDate;
}
