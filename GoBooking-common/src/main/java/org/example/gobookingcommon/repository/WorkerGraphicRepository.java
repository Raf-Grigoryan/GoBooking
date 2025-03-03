package org.example.gobookingcommon.repository;


import org.example.gobookingcommon.entity.work.WeekDay;
import org.example.gobookingcommon.entity.work.WorkGraphic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WorkerGraphicRepository extends JpaRepository<WorkGraphic, Integer> {

    List<WorkGraphic> getWorkGraphicByWorker_Id(int workerId);

    Optional<WorkGraphic> getWorkGraphicByWorker_IdAndWeekday(int id, WeekDay weekDay);

    void deleteByWorker_Id(int id);

}

