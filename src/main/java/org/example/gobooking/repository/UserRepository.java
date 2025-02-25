package org.example.gobooking.repository;


import org.example.gobooking.entity.company.Company;
import org.example.gobooking.entity.user.Role;
import org.example.gobooking.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    List<User> getUserByRole(Role role);

    Page<User> findAllByRole(Role role, Pageable pageable);

    Page<User> findUserByRoleAndEmailContaining(Role role, String email, Pageable pageable);

    List<User> findUserByCompany_Id(int companyId);

    List<User> findUserByCompany(Company company);

    @Query("SELECT COUNT(e) FROM User e WHERE YEAR(e.createdAt) = YEAR(CURRENT_DATE) AND MONTH(e.createdAt) = :month")
    int usersInMonths(@Param("month") int month);

    @Query("SELECT COUNT(u) FROM User u WHERE u.role = 'USER'")
    int countUsers();

    @Query("SELECT COUNT(u) FROM User u WHERE u.role = 'WORKER'")
    int countWorker();

    @Query("SELECT COUNT(u) FROM User u WHERE u.role = 'DIRECTOR'")
    int countDirector();


    User getUserById(int id);

    List<User> getUserByCompany_Director_Id(int directorId);
}
