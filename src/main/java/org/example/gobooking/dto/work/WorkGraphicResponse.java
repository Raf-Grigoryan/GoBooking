package org.example.gobooking.dto.work;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.gobooking.entity.work.WeekDay;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkGraphicResponse {

    private int id;
    private LocalDateTime startWorkDate;
    private LocalDateTime endedWorkDate;
    @Enumerated(EnumType.STRING)
    private WeekDay weekday;
    private boolean active;
}
