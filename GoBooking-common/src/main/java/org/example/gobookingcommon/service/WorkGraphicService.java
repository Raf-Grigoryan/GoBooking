package org.example.gobookingcommon.service;


import org.example.gobookingcommon.dto.work.EditWorkGraphicRequest;
import org.example.gobookingcommon.dto.work.WorkGraphicResponse;
import org.example.gobookingcommon.entity.user.User;
import org.example.gobookingcommon.entity.work.WorkGraphic;

import java.util.List;

public interface WorkGraphicService {

    List<String> weekDays();

    List<WorkGraphicResponse> getWorkGraphicsByWorkerId(int workerId);

    void addDefaultWorkGraphic(User worker);

    void editWorkGraphic(int workerId, EditWorkGraphicRequest editWorkGraphicRequest);

    WorkGraphicResponse getWorkGraphicById(int workGraphicId);

    WorkGraphic getWorkGraphicByWorkerIdAndDayWeek(int workerId, String dayWeek);
}
