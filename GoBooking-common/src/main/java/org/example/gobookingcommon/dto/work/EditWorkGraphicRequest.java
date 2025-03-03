package org.example.gobookingcommon.dto.work;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.gobookingcommon.entity.work.WeekDay;

import java.time.LocalTime;

@Data
@AllArgsConstructor
public class EditWorkGraphicRequest {

    @NotNull
    private int id;
    @NotNull
    private LocalTime startWorkDate;
    @NotNull
    private LocalTime endWorkDate;
    @NotNull
    private boolean active;
    @NotNull
    @Enumerated(EnumType.STRING)
    private WeekDay weekDay;
}