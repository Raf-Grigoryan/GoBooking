package org.example.gobooking.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.gobooking.customException.AlreadyRoleChangeRequestException;
import org.example.gobooking.entity.request.RoleChangeRequest;
import org.example.gobooking.repository.RoleChangeRequestRepository;
import org.example.gobooking.service.MailService;
import org.example.gobooking.service.RoleChangeRequestService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleChangeRequestServiceImpl implements RoleChangeRequestService {

    private final RoleChangeRequestRepository roleChangeRequestRepository;

    private final MailService mailService;

    @Override
    public void save(RoleChangeRequest request) {
        if (roleChangeRequestRepository.findByEmployeeAndCompany(request.getEmployee(), request.getCompany()) == null) {
            mailService.sendMailForRoleChangeRequest(request);
            roleChangeRequestRepository.save(request);
        } else {
            throw new AlreadyRoleChangeRequestException("Role change request already exists");
        }

    }
}
