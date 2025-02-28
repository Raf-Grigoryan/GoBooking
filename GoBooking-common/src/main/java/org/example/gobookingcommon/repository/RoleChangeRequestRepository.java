package org.example.gobookingcommon.repository;

import org.example.gobookingcommon.entity.company.Company;
import org.example.gobookingcommon.entity.request.RoleChangeRequest;
import org.example.gobookingcommon.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleChangeRequestRepository extends JpaRepository<RoleChangeRequest, Integer> {

    RoleChangeRequest findByEmployeeAndCompany(User user, Company company);

    int countByEmployee(User user);

    Page<RoleChangeRequest> findByEmployee(User employee, Pageable pageable);

    void deleteByCompany(Company company);
}
