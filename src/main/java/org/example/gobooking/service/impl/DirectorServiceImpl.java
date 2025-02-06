package org.example.gobooking.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.gobooking.dto.request.SaveRoleChangeRequest;
import org.example.gobooking.mapper.RoleChangeRequestMapper;
import org.example.gobooking.service.DirectorService;
import org.example.gobooking.service.RoleChangeRequestService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DirectorServiceImpl implements DirectorService {

    private final RoleChangeRequestService roleChangeRequestService;
    private final RoleChangeRequestMapper roleChangeRequestMapper;

    @Override
    public void sendWorkRequest(SaveRoleChangeRequest request) {
        roleChangeRequestService.save(roleChangeRequestMapper.toEntity(request));
    }
}
