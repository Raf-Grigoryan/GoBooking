package org.example.gobookingcommon.service;

import org.example.gobookingcommon.dto.request.RoleChangeRequestDto;
import org.example.gobookingcommon.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RoleChangeRequestService {
    void save(org.example.gobookingcommon.entity.request.RoleChangeRequest request);
    
    Page<RoleChangeRequestDto> findByEmployee(User user, Pageable pageable);

    void agree(int companyId, boolean agree, User user);
}
