package org.example.gobooking.service;


import org.example.gobooking.dto.auth.PasswordChangeRequest;
import org.example.gobooking.dto.auth.UserEditRequest;
import org.example.gobooking.dto.user.SaveUserRequest;
import org.example.gobooking.entity.user.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface UserService {

    void register(SaveUserRequest saveUserRequest);

    Optional<User> findByEmail(String email);

    void verifyUserAccount(String email, String token);

    List<String> getAdminsEmails();

    User getUserById(int id);

    void saveUser(User user);

    void changePassword(User user, PasswordChangeRequest passwordChangeRequest);

    boolean editUser(User currentUser,UserEditRequest user, MultipartFile image);

}
