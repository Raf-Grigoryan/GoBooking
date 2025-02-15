package org.example.gobooking.repository;

import org.example.gobooking.entity.work.WeekDay;
import org.example.gobooking.entity.work.WorkGraphic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WorkerGraphicRepository extends JpaRepository<WorkGraphic, Integer> {

    List<WorkGraphic> getWorkGraphicByWorker_Id(int workerId);

    Optional<WorkGraphic> getWorkGraphicByWorker_IdAndWeekday(int id, WeekDay weekDay);
}

