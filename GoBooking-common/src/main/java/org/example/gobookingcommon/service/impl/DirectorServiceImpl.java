package org.example.gobookingcommon.service.impl;

import lombok.RequiredArgsConstructor;

import org.example.gobookingcommon.dto.company.CompanyManagement;
import org.example.gobookingcommon.dto.company.CompanyResponse;
import org.example.gobookingcommon.dto.request.SaveRoleChangeRequest;
import org.example.gobookingcommon.mapper.RoleChangeRequestMapper;
import org.example.gobookingcommon.service.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DirectorServiceImpl implements DirectorService {

    private final RoleChangeRequestService roleChangeRequestService;

    private final RoleChangeRequestMapper roleChangeRequestMapper;

    private final UserService userService;

    private final CompanyService companyService;

    private final WorkService workService;

    private final BookingService bookingService;

    @Override
    public void sendWorkRequest(SaveRoleChangeRequest request) {
        roleChangeRequestService.save(roleChangeRequestMapper.toEntity(request));
    }

    @Override
    public CompanyManagement getCompanyManagementByDirectorId(int directorId) {
        CompanyResponse companyResponse = companyService.getCompanyResponseByDirectorId(directorId);
        return CompanyManagement.builder()
                .workers(userService.getWorkersByDirectorId(directorId))
                .company(companyResponse)
                .serviceCount(workService.getWorkerServiceCountByDirector(directorId))
                .bookingCount(bookingService.getBookingCountByCompanyId(companyResponse.getId()))
                .monthEarning(bookingService.getMonthEarningByCompanyId(companyResponse.getId()))
                .companyBookingStatistics(bookingService.getRandomServicesByCompanyId(companyResponse.getId()))
                .serviceList(workService.getServicesByCompanyId(companyResponse.getId()))
                .finishedBookings(bookingService.getFinishedBookingsByCompanyId(companyResponse.getId()))
                .build();
    }


}
