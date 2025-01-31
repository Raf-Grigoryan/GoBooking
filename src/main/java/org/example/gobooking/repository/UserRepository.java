package org.example.gobooking.repository;



import org.example.gobooking.entity.user.Role;
import org.example.gobooking.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);


    List<User> getUserByRole(Role role);
}
