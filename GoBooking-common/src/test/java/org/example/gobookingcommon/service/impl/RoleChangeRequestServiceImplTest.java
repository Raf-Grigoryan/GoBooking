package org.example.gobookingcommon.service.impl;


import org.example.gobookingcommon.customException.AlreadyRoleChangeRequestException;
import org.example.gobookingcommon.entity.company.Company;
import org.example.gobookingcommon.entity.request.RoleChangeRequest;
import org.example.gobookingcommon.entity.user.User;
import org.example.gobookingcommon.repository.RoleChangeRequestRepository;
import org.example.gobookingcommon.service.CompanyService;
import org.example.gobookingcommon.service.MailService;
import org.example.gobookingcommon.service.UserService;
import org.example.gobookingcommon.service.WorkGraphicService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleChangeRequestServiceImplTest {

    @Test
    void testSave_Success() {
        RoleChangeRequestRepository roleChangeRequestRepository = mock(RoleChangeRequestRepository.class);
        MailService mailService = mock(MailService.class);

        RoleChangeRequestServiceImpl roleChangeRequestService = new RoleChangeRequestServiceImpl(
                roleChangeRequestRepository, mailService, null, null, null, null);

        RoleChangeRequest request = new RoleChangeRequest();
        request.setEmployee(new User());
        request.setCompany(new Company());

        when(roleChangeRequestRepository.findByEmployeeAndCompany(request.getEmployee(), request.getCompany()))
                .thenReturn(null);

        roleChangeRequestService.save(request);

    }

    @Test
    void testSave_AlreadyExists() {
        RoleChangeRequestRepository roleChangeRequestRepository = mock(RoleChangeRequestRepository.class);
        MailService mailService = mock(MailService.class);

        RoleChangeRequestServiceImpl roleChangeRequestService = new RoleChangeRequestServiceImpl(
                roleChangeRequestRepository, mailService, null, null, null, null);

        RoleChangeRequest request = new RoleChangeRequest();
        request.setEmployee(new User());
        request.setCompany(new Company());

        when(roleChangeRequestRepository.findByEmployeeAndCompany(request.getEmployee(), request.getCompany()))
                .thenReturn(request);

        assertThrows(AlreadyRoleChangeRequestException.class, () -> {
            roleChangeRequestService.save(request);
        });
    }



    @Test
    void testAgree_Approve() {
        RoleChangeRequestRepository roleChangeRequestRepository = mock(RoleChangeRequestRepository.class);
        CompanyService companyService = mock(CompanyService.class);
        UserService userService = mock(UserService.class);
        MailService mailService = mock(MailService.class);
        WorkGraphicService workGraphicService = mock(WorkGraphicService.class);
        RoleChangeRequestServiceImpl roleChangeRequestService = new RoleChangeRequestServiceImpl(
                roleChangeRequestRepository, mailService, null, userService, companyService, workGraphicService);

        Company company = new Company();
        company.setDirector(new User());
        User user = new User();
        user.setName("John Doe");
        int companyId = 1;

        when(companyService.getCompanyById(companyId)).thenReturn(company);

        doNothing().when(userService).saveUser(any(User.class));
        doNothing().when(workGraphicService).addDefaultWorkGraphic(any(User.class));

        roleChangeRequestService.agree(companyId, true, user);

        verify(mailService).sendMailForRoleChangeRequestAgree(eq(company.getDirector().getEmail()), eq(user.getName()));
        verify(userService).saveUser(user);
        verify(workGraphicService).addDefaultWorkGraphic(user);
        verify(roleChangeRequestRepository).deleteByCompany(company);
    }


}