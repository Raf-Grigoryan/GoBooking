package org.example.gobookingcommon.mapper;


import org.example.gobookingcommon.dto.work.WorkGraphicResponse;
import org.example.gobookingcommon.entity.work.WorkGraphic;
import org.example.gobookingcommon.service.CompanyService;
import org.example.gobookingcommon.service.UserService;
import org.mapstruct.Mapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Mapper(componentModel = "spring",
        uses = {UserService.class, CompanyService.class},
        imports = {UserService.class, CompanyService.class})
public interface WorkGraphicMapper {


    WorkGraphicResponse toWorkGraphicResponse(WorkGraphic workGraphic);

    List<WorkGraphicResponse> toWorkGraphicResponse(List<WorkGraphic> workGraphics);

    default LocalDateTime map(LocalTime localTime) {
        return LocalDateTime.of(LocalDate.now(), localTime);
    }
}
