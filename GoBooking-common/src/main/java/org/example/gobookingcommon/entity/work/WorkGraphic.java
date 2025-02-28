package org.example.gobookingcommon.entity.work;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.gobookingcommon.entity.user.User;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class WorkGraphic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private LocalTime startWorkDate;
    private LocalTime endedWorkDate;
    @ManyToOne
    private User worker;
    @Enumerated(EnumType.STRING)
    private WeekDay weekday;
    private boolean active;
}
