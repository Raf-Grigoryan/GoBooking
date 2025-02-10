package org.example.gobooking.service;

import org.example.gobooking.dto.request.RoleChangeRequestDto;
import org.example.gobooking.entity.request.RoleChangeRequest;
import org.example.gobooking.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RoleChangeRequestService {
    void save(RoleChangeRequest request);

    int countByEmployee(User user);

    Page<RoleChangeRequestDto> findByEmployee(User user, Pageable pageable);

    void agree(int companyId, boolean agree, User user);
}
