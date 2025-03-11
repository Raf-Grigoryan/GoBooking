package org.example.gobookingcommon.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.example.gobookingcommon.customException.UnauthorizedWorkGraphicModificationException;
import org.example.gobookingcommon.dto.work.EditWorkGraphicRequest;
import org.example.gobookingcommon.dto.work.WorkGraphicResponse;
import org.example.gobookingcommon.entity.user.User;
import org.example.gobookingcommon.entity.work.WeekDay;
import org.example.gobookingcommon.entity.work.WorkGraphic;
import org.example.gobookingcommon.mapper.WorkGraphicMapper;
import org.example.gobookingcommon.repository.WorkerGraphicRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class WorkGraphicServiceImplTest {

    @Mock
    private WorkGraphicMapper workGraphicMapper;

    @Mock
    private WorkerGraphicRepository workerGraphicRepository;

    @InjectMocks
    private WorkGraphicServiceImpl workGraphicService;

    @Test
    void weekDays_ShouldReturnAllWeekDays() {
        List<String> result = workGraphicService.weekDays();
        assertNotNull(result);
        assertEquals(7, result.size());
        assertTrue(result.contains("MONDAY"));
        assertTrue(result.contains("SUNDAY"));
    }


    @Test
    void getWorkGraphicsByWorkerId_ShouldReturnWorkGraphicResponses() {
        int workerId = 1;
        List<WorkGraphicResponse> expectedResponses = Collections.singletonList(new WorkGraphicResponse());
        List<WorkGraphic> workGraphicList = Collections.emptyList();

        when(workerGraphicRepository.getWorkGraphicByWorker_Id(workerId)).thenReturn(workGraphicList);
        when(workGraphicMapper.toWorkGraphicResponse(ArgumentMatchers.<List<WorkGraphic>>any())).thenReturn(expectedResponses);

        List<WorkGraphicResponse> result = workGraphicService.getWorkGraphicsByWorkerId(workerId);

        assertNotNull(result);
        assertEquals(expectedResponses, result);
        verify(workerGraphicRepository).getWorkGraphicByWorker_Id(workerId);
        verify(workGraphicMapper).toWorkGraphicResponse(ArgumentMatchers.<List<WorkGraphic>>any());
    }


    @Test
    void addDefaultWorkGraphic_ShouldSaveWorkGraphicsForEachWeekday() {
        User worker = mock(User.class);
        WeekDay[] weekDays = WeekDay.values();

        workGraphicService.addDefaultWorkGraphic(worker);

        verify(workerGraphicRepository, times(weekDays.length)).save(any(WorkGraphic.class));
    }

    @Test
    void editWorkGraphic_ShouldUpdateAndSaveWorkGraphic() {
        int workerId = 1;
        WeekDay weekDay = WeekDay.MONDAY;
        EditWorkGraphicRequest request = new EditWorkGraphicRequest(workerId, LocalTime.of(9, 0), LocalTime.of(17, 0), true, weekDay);
        User worker = new User();
        worker.setId(workerId);
        WorkGraphic workGraphic = new WorkGraphic();
        workGraphic.setWorker(worker);

        when(workerGraphicRepository.getWorkGraphicByWorker_IdAndWeekday(workerId, weekDay)).thenReturn(Optional.of(workGraphic));

        workGraphicService.editWorkGraphic(workerId, request);

        assertEquals(request.getStartWorkDate(), workGraphic.getStartWorkDate());
        assertEquals(request.getEndWorkDate(), workGraphic.getEndedWorkDate());
        assertEquals(request.isActive(), workGraphic.isActive());
        verify(workerGraphicRepository).save(workGraphic);
    }

    @Test
    void editWorkGraphic_ShouldThrowEntityNotFoundException_WhenWorkGraphicNotFound() {
        int workerId = 1;
        EditWorkGraphicRequest request = new EditWorkGraphicRequest(workerId, LocalTime.of(9, 0), LocalTime.of(17, 0), true, WeekDay.MONDAY);

        when(workerGraphicRepository.getWorkGraphicByWorker_IdAndWeekday(workerId, request.getWeekDay())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> workGraphicService.editWorkGraphic(workerId, request));
    }

    @Test
    void editWorkGraphic_ShouldThrowUnauthorizedWorkGraphicModificationException_WhenWorkerIdDoesNotMatch() {
        int workerId = 1;
        int differentWorkerId = 2;
        WeekDay weekDay = WeekDay.MONDAY;
        EditWorkGraphicRequest request = new EditWorkGraphicRequest(workerId, LocalTime.of(9, 0), LocalTime.of(17, 0), true, weekDay);
        User differentWorker = new User();
        differentWorker.setId(differentWorkerId);
        WorkGraphic workGraphic = new WorkGraphic();
        workGraphic.setWorker(differentWorker);

        when(workerGraphicRepository.getWorkGraphicByWorker_IdAndWeekday(workerId, weekDay)).thenReturn(Optional.of(workGraphic));

        assertThrows(UnauthorizedWorkGraphicModificationException.class, () -> workGraphicService.editWorkGraphic(workerId, request));
    }


    @Test
    void getWorkGraphicById_ShouldReturnWorkGraphicResponse_WhenFound() {
        int workGraphicId = 1;
        WorkGraphic workGraphic = new WorkGraphic();
        WorkGraphicResponse expectedResponse = new WorkGraphicResponse();

        when(workerGraphicRepository.findById(workGraphicId)).thenReturn(Optional.of(workGraphic));
        when(workGraphicMapper.toWorkGraphicResponse(workGraphic)).thenReturn(expectedResponse);

        WorkGraphicResponse result = workGraphicService.getWorkGraphicById(workGraphicId);

        assertNotNull(result);
        assertEquals(expectedResponse, result);
        verify(workerGraphicRepository).findById(workGraphicId);
        verify(workGraphicMapper).toWorkGraphicResponse(workGraphic);
    }

    @Test
    void getWorkGraphicById_ShouldThrowException_WhenNotFound() {
        int workGraphicId = 1;
        when(workerGraphicRepository.findById(workGraphicId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> workGraphicService.getWorkGraphicById(workGraphicId));
        assertEquals("Work graphic not found for ID: " + workGraphicId, exception.getMessage());
    }


    @Test
    void getWorkGraphicByWorkerIdAndDayWeek_ShouldReturnWorkGraphic_WhenFound() {
        int workerId = 1;
        String dayWeek = "MONDAY";
        WorkGraphic workGraphic = new WorkGraphic();

        when(workerGraphicRepository.getWorkGraphicByWorker_IdAndWeekday(workerId, WeekDay.valueOf(dayWeek)))
                .thenReturn(Optional.of(workGraphic));

        WorkGraphic result = workGraphicService.getWorkGraphicByWorkerIdAndDayWeek(workerId, dayWeek);

        assertNotNull(result);
        assertEquals(workGraphic, result);
        verify(workerGraphicRepository).getWorkGraphicByWorker_IdAndWeekday(workerId, WeekDay.valueOf(dayWeek));
    }

    @Test
    void getWorkGraphicByWorkerIdAndDayWeek_ShouldThrowEntityNotFoundException_WhenNotFound() {
        int workerId = 1;
        String dayWeek = "MONDAY";

        when(workerGraphicRepository.getWorkGraphicByWorker_IdAndWeekday(workerId, WeekDay.valueOf(dayWeek)))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> workGraphicService.getWorkGraphicByWorkerIdAndDayWeek(workerId, dayWeek));
    }




}
