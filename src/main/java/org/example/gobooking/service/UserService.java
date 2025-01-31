package org.example.gobooking.service;



import org.example.gobooking.dto.user.SaveUserRequest;
import org.example.gobooking.entity.user.User;

import java.util.Optional;

public interface UserService {

    void register(SaveUserRequest saveUserRequest);

    Optional<User> findByEmail(String email);

    void verifyUserAccount(String email, String token);

    List<String> getAdminsEmails();

    User getUserById(int id);


    User getUserById(int id);
}
