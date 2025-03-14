package org.example.gobookingcommon.service.impl;


import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.gobookingcommon.customException.UnauthorizedWorkGraphicModificationException;
import org.example.gobookingcommon.dto.work.EditWorkGraphicRequest;
import org.example.gobookingcommon.dto.work.WorkGraphicResponse;
import org.example.gobookingcommon.entity.user.User;
import org.example.gobookingcommon.entity.work.WeekDay;
import org.example.gobookingcommon.entity.work.WorkGraphic;
import org.example.gobookingcommon.repository.WorkerGraphicRepository;
import org.example.gobookingcommon.service.WorkGraphicService;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WorkGraphicServiceImpl implements WorkGraphicService {

    private final org.example.gobookingcommon.mapper.WorkGraphicMapper workGraphicMapper;

    private final WorkerGraphicRepository workerGraphicRepository;

    @Override
    public List<String> weekDays() {
        List<String> weekDays = new ArrayList<>();
        for (WeekDay weekDay : WeekDay.values()) {
            weekDays.add(weekDay.name());
        }
        return weekDays;
    }

    @Override
    public List<WorkGraphicResponse> getWorkGraphicsByWorkerId(int workerId) {
        return workGraphicMapper.toWorkGraphicResponse(workerGraphicRepository.getWorkGraphicByWorker_Id(workerId));
    }

    @Override
    public void addDefaultWorkGraphic(User worker) {
        WeekDay[] values = WeekDay.values();
        for (WeekDay value : values) {
            WorkGraphic workGraphic = WorkGraphic.builder()
                    .startWorkDate(LocalTime.of(10, 0))
                    .endedWorkDate(LocalTime.of(18, 0))
                    .worker(worker)
                    .weekday(value)
                    .active(true)
                    .build();
            workerGraphicRepository.save(workGraphic);
        }
    }

    @Override
    public void editWorkGraphic(int workerId, EditWorkGraphicRequest editWorkGraphicRequest) {
        Optional<WorkGraphic> workGraphic = workerGraphicRepository.getWorkGraphicByWorker_IdAndWeekday(workerId, editWorkGraphicRequest.getWeekDay());
        if (workGraphic.isPresent()) {
            WorkGraphic workGraphicEntity = workGraphic.get();
            if (workGraphicEntity.getWorker().getId() == workerId) {
                workGraphicEntity.setStartWorkDate(editWorkGraphicRequest.getStartWorkDate());
                workGraphicEntity.setEndedWorkDate(editWorkGraphicRequest.getEndWorkDate());
                workGraphicEntity.setActive(editWorkGraphicRequest.isActive());
                workerGraphicRepository.save(workGraphicEntity);
            } else {
                throw new UnauthorizedWorkGraphicModificationException("You are not allowed to edit this work graphic");
            }
        } else {
            throw new EntityNotFoundException("Work graphic not found");
        }

    }

    @Override
    public WorkGraphicResponse getWorkGraphicById(int workGraphicId) {
        Optional<WorkGraphic> workGraphic = workerGraphicRepository.findById(workGraphicId);

        return workGraphic
                .map(workGraphicMapper::toWorkGraphicResponse)
                .orElseThrow(() -> new RuntimeException("Work graphic not found for ID: " + workGraphicId));
    }

    @Override
    public WorkGraphic getWorkGraphicByWorkerIdAndDayWeek(int workerId, String dayWeek) {
        Optional<WorkGraphic> workGraphic = workerGraphicRepository.getWorkGraphicByWorker_IdAndWeekday(workerId, WeekDay.valueOf(dayWeek));
        return workGraphic.orElseThrow(() -> new EntityNotFoundException("Work graphic not found"));
    }

    public void deleteWorkGraphic(int workGraphicId) {
        workerGraphicRepository.deleteByWorker_Id(workGraphicId);
    }
}
