package org.example.gobooking.repository;


import org.example.gobooking.entity.company.Company;
import org.example.gobooking.entity.user.Role;
import org.example.gobooking.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    List<User> getUserByRole(Role role);

    Page<User> findAllByRole(Role role, Pageable pageable);

    Page<User> findUserByRoleAndEmailContaining(Role role, String email, Pageable pageable);

    List<User> findUserByCompany_Id(int companyId);

    List<User> findUserByCompany(Company company);

    User getUserById(int id);

    List<User> getUserByCompany_Director_Id(int directorId);
}
