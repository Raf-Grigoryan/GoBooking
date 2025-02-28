package org.example.gobookingcommon.service;


import org.example.gobookingcommon.dto.auth.PasswordChangeRequest;
import org.example.gobookingcommon.dto.auth.UserAuthRequest;
import org.example.gobookingcommon.dto.auth.UserAuthResponse;
import org.example.gobookingcommon.dto.auth.UserEditRequest;
import org.example.gobookingcommon.dto.user.SaveUserRequest;
import org.example.gobookingcommon.dto.user.UserDto;
import org.example.gobookingcommon.dto.user.WorkerResponse;
import org.example.gobookingcommon.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    Page<UserDto> getAllUsers(PageRequest pageRequest);

    Page<UserDto> getAllUsersByEmail(PageRequest pageRequest, String keyword);

    void changePassword(User user, PasswordChangeRequest passwordChangeRequest);

    boolean editUser(User currentUser, UserEditRequest user, MultipartFile image);

    void editUser(User user);

    List<WorkerResponse> workersByCompanyId(int companyId);

    WorkerResponse getWorkerById(int id);

    void delete(User user);

    List<Integer> analyticUsers();

    List<Integer> getAllRolesUsersCount();

    int grtRoleChangeRequestCount(User user);

    List<WorkerResponse> getWorkersByDirectorId(int directorId);

    UserAuthResponse login(UserAuthRequest userAuthRequest);
}
