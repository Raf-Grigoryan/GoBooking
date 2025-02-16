package org.example.gobooking.service;

import org.example.gobooking.dto.work.EditWorkGraphicRequest;
import org.example.gobooking.dto.work.WorkGraphicResponse;
import org.example.gobooking.entity.user.User;
import org.example.gobooking.entity.work.WorkGraphic;

import java.util.List;

public interface WorkGraphicService {

    List<String> weekDays();

    List<WorkGraphicResponse> getWorkGraphicsByWorkerId(int workerId);

    void addDefaultWorkGraphic(User worker);

    void editWorkGraphic(int workerId, EditWorkGraphicRequest editWorkGraphicRequest);

    WorkGraphicResponse getWorkGraphicById(int workGraphicId);

    WorkGraphic getWorkGraphicByWorkerIdAndDayWeek(int workerId, String dayWeek);
}
