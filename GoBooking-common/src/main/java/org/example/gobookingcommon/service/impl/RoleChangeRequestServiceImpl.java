package org.example.gobookingcommon.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.gobookingcommon.customException.AlreadyRoleChangeRequestException;
import org.example.gobookingcommon.dto.request.RoleChangeRequestDto;
import org.example.gobookingcommon.entity.company.Company;
import org.example.gobookingcommon.entity.request.RoleChangeRequest;
import org.example.gobookingcommon.entity.user.Role;
import org.example.gobookingcommon.entity.user.User;
import org.example.gobookingcommon.mapper.RoleChangeRequestMapper;
import org.example.gobookingcommon.repository.RoleChangeRequestRepository;
import org.example.gobookingcommon.service.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleChangeRequestServiceImpl implements RoleChangeRequestService {

    private final RoleChangeRequestRepository roleChangeRequestRepository;

    private final MailService mailService;

    private final RoleChangeRequestMapper roleChangeRequestMapper;

    private final UserService userService;

    private final CompanyService companyService;

    private final WorkGraphicService workGraphicService;

    @Override
    public void save(RoleChangeRequest request) {
        if (roleChangeRequestRepository.findByEmployeeAndCompany(request.getEmployee(), request.getCompany()) == null) {
            mailService.sendMailForRoleChangeRequest(request);
            roleChangeRequestRepository.save(request);
        } else {
            throw new AlreadyRoleChangeRequestException("Role change request already exists");
        }

    }

    @Override
    public Page<RoleChangeRequestDto> findByEmployee(User user, Pageable pageable) {
        Page<RoleChangeRequest> result = roleChangeRequestRepository.findByEmployee(user, pageable);
        return result.map(roleChangeRequestMapper::toDto);
    }

    @Transactional
    @Override
    public void agree(int companyId, boolean agree, User user) {
        Company company = companyService.getCompanyById(companyId);
        if (agree) {
            mailService.sendMailForRoleChangeRequestAgree(company.getDirector().getEmail(), user.getName());
            user.setCompany(company);
            user.setRole(Role.WORKER);
            userService.saveUser(user);
            workGraphicService.addDefaultWorkGraphic(user);
        } else {
            mailService.sendMailForRoleChangeRequestDisagree(company.getDirector().getEmail(), user.getName());
        }
        roleChangeRequestRepository.deleteByCompany(company);
    }



}
