package org.example.gobooking.repository;

import org.example.gobooking.entity.company.Company;
import org.example.gobooking.entity.request.RoleChangeRequest;
import org.example.gobooking.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleChangeRequestRepository extends JpaRepository<RoleChangeRequest, Integer> {

    RoleChangeRequest findByEmployeeAndCompany(User user, Company company);
}
