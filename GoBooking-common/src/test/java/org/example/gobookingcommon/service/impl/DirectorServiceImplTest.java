package org.example.gobookingcommon.service.impl;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.example.gobookingcommon.customException.SubscriptionNotValidException;
import org.example.gobookingcommon.dto.booking.WorkerBookingResponse;
import org.example.gobookingcommon.dto.company.CompanyManagement;
import org.example.gobookingcommon.dto.company.CompanyResponse;

import org.example.gobookingcommon.dto.request.SaveRoleChangeRequest;
import org.example.gobookingcommon.dto.subscription.BookingStatistics;
import org.example.gobookingcommon.dto.user.WorkerResponse;
import org.example.gobookingcommon.dto.work.ServiceResponse;


import org.example.gobookingcommon.entity.company.Company;
import org.example.gobookingcommon.entity.request.RoleChangeRequest;
import org.example.gobookingcommon.entity.subscription.ValidSubscription;
import org.example.gobookingcommon.entity.user.User;
import org.example.gobookingcommon.mapper.RoleChangeRequestMapper;
import org.example.gobookingcommon.repository.ValidSubscriptionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class DirectorServiceTest {


    @Mock
    private CompanyServiceImpl companyService;

    @Mock
    private UserServiceImpl userService;

    @Mock
    private WorkServiceImpl workService;

    @Mock
    private BookingServiceImpl bookingService;

    @Mock
    private ValidSubscriptionRepository validSubscriptionRepository;

    @Mock
    private RoleChangeRequestServiceImpl roleChangeRequestService;

    @Mock
    private SaveRoleChangeRequest saveRoleChangeRequest;

    @Mock
    private RoleChangeRequestMapper roleChangeRequestMapper;

    @Mock
    private User user;

    @Mock
    private ValidSubscription validSubscription;



    @InjectMocks
    private DirectorServiceImpl companyManagementService;

    private final int directorId = 1;
    private final int companyId = 100;

    @BeforeEach
    void setUp() {
        CompanyResponse companyResponse = new CompanyResponse();
        companyResponse.setId(companyId);

        lenient().when(companyService.getCompanyResponseByDirectorId(directorId)).thenReturn(companyResponse);
        lenient().when(userService.getWorkersByDirectorId(directorId)).thenReturn(List.of(new WorkerResponse()));
        lenient().when(workService.getWorkerServiceCountByDirector(directorId)).thenReturn(5);
        lenient().when(bookingService.getBookingCountByCompanyId(companyId)).thenReturn(10);
        lenient().when(bookingService.getMonthEarningByCompanyId(companyId)).thenReturn(5000.00);
        lenient().when(workService.getServicesByCompanyId(companyId)).thenReturn(List.of(new ServiceResponse()));
        lenient().when(bookingService.getFinishedBookingsByCompanyId(companyId)).thenReturn(List.of(new WorkerBookingResponse()));
        lenient().when(bookingService.getRandomServicesByCompanyId(companyId)).thenReturn(new BookingStatistics());
    }

    @Test
    void testGetCompanyManagementByDirectorId() {
        CompanyResponse companyResponse = new CompanyResponse();
        companyResponse.setId(companyId);

        when(companyService.getCompanyResponseByDirectorId(directorId)).thenReturn(companyResponse);
        when(userService.getWorkersByDirectorId(directorId)).thenReturn(List.of(new WorkerResponse()));
        when(workService.getWorkerServiceCountByDirector(directorId)).thenReturn(5);
        when(bookingService.getBookingCountByCompanyId(companyId)).thenReturn(10);
        when(bookingService.getMonthEarningByCompanyId(companyId)).thenReturn(5000.00);
        when(workService.getServicesByCompanyId(companyId)).thenReturn(List.of(new ServiceResponse()));
        when(bookingService.getFinishedBookingsByCompanyId(companyId)).thenReturn(List.of(new WorkerBookingResponse()));
        when(bookingService.getRandomServicesByCompanyId(companyId)).thenReturn(new BookingStatistics());

        CompanyManagement companyManagement = companyManagementService.getCompanyManagementByDirectorId(directorId);

        assertNotNull(companyManagement);
        assertNotNull(companyManagement.getCompany());
        assertNotNull(companyManagement.getWorkers());
        assertNotNull(companyManagement.getServiceList());
        assertNotNull(companyManagement.getCompanyBookingStatistics());
        assertNotNull(companyManagement.getFinishedBookings());

        assertEquals(5, companyManagement.getServiceCount());
        assertEquals(10, companyManagement.getBookingCount());
        assertEquals(5000, companyManagement.getMonthEarning());

        verify(companyService).getCompanyResponseByDirectorId(directorId);
        verify(userService).getWorkersByDirectorId(directorId);
        verify(workService).getWorkerServiceCountByDirector(directorId);
        verify(bookingService).getBookingCountByCompanyId(companyId);
        verify(bookingService).getMonthEarningByCompanyId(companyId);
        verify(workService).getServicesByCompanyId(companyId);
        verify(bookingService).getFinishedBookingsByCompanyId(companyId);
        verify(bookingService).getRandomServicesByCompanyId(companyId);
    }

    @Test
    void testGetCompanyManagementByDirectorId_WithCompanyNotFound() {
        when(companyService.getCompanyResponseByDirectorId(directorId)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> companyManagementService.getCompanyManagementByDirectorId(directorId));
    }

    @Test
    void testGetCompanyManagementByDirectorId_WithEmptyValues() {
        CompanyResponse companyResponse = new CompanyResponse();
        companyResponse.setId(companyId);
        when(companyService.getCompanyResponseByDirectorId(directorId)).thenReturn(companyResponse);

        when(userService.getWorkersByDirectorId(directorId)).thenReturn(Collections.emptyList());
        when(workService.getWorkerServiceCountByDirector(directorId)).thenReturn(0);
        when(bookingService.getBookingCountByCompanyId(companyId)).thenReturn(0);
        when(bookingService.getMonthEarningByCompanyId(companyId)).thenReturn(0.0);
        when(workService.getServicesByCompanyId(companyId)).thenReturn(Collections.emptyList());
        when(bookingService.getFinishedBookingsByCompanyId(companyId)).thenReturn(Collections.emptyList());

        CompanyManagement companyManagement = companyManagementService.getCompanyManagementByDirectorId(directorId);

        assertNotNull(companyManagement);
        assertNotNull(companyManagement.getCompany());
        assertTrue(companyManagement.getWorkers().isEmpty());
        assertTrue(companyManagement.getServiceList().isEmpty());
        assertTrue(companyManagement.getFinishedBookings().isEmpty());

        assertEquals(0, companyManagement.getServiceCount());
        assertEquals(0, companyManagement.getBookingCount());
        assertEquals(0, companyManagement.getMonthEarning());
    }

    @Test
    void testGetCompanyManagementByDirectorId_WithServiceException() {
        CompanyResponse companyResponse = new CompanyResponse();
        companyResponse.setId(companyId);
        when(companyService.getCompanyResponseByDirectorId(directorId)).thenReturn(companyResponse);

        when(userService.getWorkersByDirectorId(directorId)).thenThrow(new RuntimeException("Service Error"));

        assertThrows(RuntimeException.class, () -> companyManagementService.getCompanyManagementByDirectorId(directorId));

        verify(userService).getWorkersByDirectorId(directorId);
    }

    @Test
    public void testSendWorkRequest_SubscriptionValid_RequestSaved() {
        when(companyService.getCompanyByDirector(user)).thenReturn(new Company());
        when(validSubscriptionRepository.findByCompany(any(Company.class))).thenReturn(validSubscription);
        when(roleChangeRequestMapper.toEntity(saveRoleChangeRequest)).thenReturn(new RoleChangeRequest());

        companyManagementService.sendWorkRequest(saveRoleChangeRequest, user);

        verify(roleChangeRequestService, times(1)).save(any(RoleChangeRequest.class));
    }

    @Test
    public void testSendWorkRequest_SubscriptionNotValid_ExceptionThrown() {
        when(companyService.getCompanyByDirector(user)).thenReturn(new Company());
        when(validSubscriptionRepository.findByCompany(any(Company.class))).thenReturn(null);

        assertThrows(SubscriptionNotValidException.class, () -> companyManagementService.sendWorkRequest(saveRoleChangeRequest, user));
    }

    @Test
    public void testSendWorkRequest_SubscriptionValid_RequestMapperCalled() {
        when(companyService.getCompanyByDirector(user)).thenReturn(new Company());
        when(validSubscriptionRepository.findByCompany(any(Company.class))).thenReturn(validSubscription);

        companyManagementService.sendWorkRequest(saveRoleChangeRequest, user);

        verify(roleChangeRequestMapper, times(1)).toEntity(saveRoleChangeRequest);
    }
}