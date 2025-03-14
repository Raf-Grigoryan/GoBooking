package org.example.gobookingcommon.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.gobookingcommon.customException.*;
import org.example.gobookingcommon.dto.auth.PasswordChangeRequest;
import org.example.gobookingcommon.dto.auth.UserAuthRequest;
import org.example.gobookingcommon.dto.auth.UserAuthResponse;
import org.example.gobookingcommon.dto.auth.UserEditRequest;
import org.example.gobookingcommon.dto.user.SaveUserRequest;
import org.example.gobookingcommon.dto.user.UserDto;
import org.example.gobookingcommon.dto.user.WorkerResponse;
import org.example.gobookingcommon.entity.user.Role;
import org.example.gobookingcommon.entity.user.User;
import org.example.gobookingcommon.mapper.UserMapper;
import org.example.gobookingcommon.repository.CompanyRepository;
import org.example.gobookingcommon.repository.PromotionRequestsRepository;
import org.example.gobookingcommon.repository.UserRepository;
import org.example.gobookingcommon.service.MailService;
import org.example.gobookingcommon.service.UserService;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final MailService mailService;

    private final CompanyRepository companyRepository;

    private final WorkGraphicServiceImpl workGraphicService;

    private final PromotionRequestsRepository promotionRequestsRepository;

    @Value("${image.upload.path}")
    private String imageUploadPath;

    private final UserMapper userMapper;

    @Override
    public void register(SaveUserRequest saveUserRequest) {
        Optional<User> userDb = userRepository.findByEmail(saveUserRequest.getEmail());
        if (userDb.isPresent()) {
            throw new UserOnlyExistException("User with this email " + saveUserRequest.getEmail() + " already exist ");
        }
        UUID token = UUID.randomUUID();
        userRepository.save(User.builder()
                .name(saveUserRequest.getName())
                .surname(saveUserRequest.getSurname())
                .email(saveUserRequest.getEmail())
                .password(passwordEncoder.encode(saveUserRequest.getPassword()))
                .phone(saveUserRequest.getPhone())
                .role(Role.USER)
                .pictureName("def.webp")
                .accountNonLocked(true)
                .createdAt(new Date())
                .token(token.toString())
                .build());
        mailService.sendMailForUserVerify(saveUserRequest.getEmail(), token.toString());
        log.info("User registered with {} in this time {}", saveUserRequest.getEmail(), new Date());
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void verifyUserAccount(String email, String token) {
        Optional<User> userDb = userRepository.findByEmail(email);
        if (userDb.isEmpty()) {
            throw new CannotVerifyUserException("Error: Cannot verify user account. User with email " + email + " not found.");
        }
        User user = userDb.get();
        if (user.getToken() == null || !user.getToken().equals(token)) {
            throw new CannotVerifyUserException("Error: Invalid verification token.");
        }
        user.setEnable(true);
        user.setToken(null);
        userRepository.save(user);
    }


    @Override
    public List<String> getAdminsEmails() {
        List<String> adminEmails = new ArrayList<>();
        List<User> adminsDb = userRepository.getUserByRole((Role.ADMIN));
        for (User admin : adminsDb) {
            adminEmails.add(admin.getEmail());
        }
        return adminEmails;
    }

    @Override
    @Named("getUserById")
    public User getUserById(int id) {
        Optional<User> userDb = userRepository.findById(id);
        if (userDb.isPresent()) {
            return userDb.get();
        }
        throw new CannotVerifyUserException("Error: Cannot find user with id " + id);
    }

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    public void changePassword(User user, PasswordChangeRequest passwordChangeRequest) {
        if (!passwordEncoder.matches(passwordChangeRequest.getOldPassword(), user.getPassword())) {
            throw new PasswordIncorrectException("Error: Old password does not match");
        }
        if (!passwordChangeRequest.getNewPassword().equals(passwordChangeRequest.getConfirmPassword())) {
            throw new ConfirmPasswordIncorrectException("Error: Confirm password does not match");
        }
        user.setPassword(passwordEncoder.encode(passwordChangeRequest.getNewPassword()));
        userRepository.save(user);
        mailService.sendMailForChangePassword(user.getEmail(), passwordChangeRequest.getNewPassword());
        log.info("User password changed with {} in this time {}", user.getEmail(), new Date());
    }

    @Override
    public boolean editUser(User currentUser, UserEditRequest user, MultipartFile image) {
        try {
            currentUser.setName(user.getName());
            currentUser.setSurname(user.getSurname());
            currentUser.setPhone(user.getPhone());
            if (image != null && !image.isEmpty()) {
                if (!isValidImage(image)) {
                    throw new IllegalArgumentException("Invalid image format");
                }
                String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
                File file = new File(imageUploadPath, fileName);
                image.transferTo(file);
                currentUser.setPictureName(fileName);
            }
            if (!currentUser.getEmail().equals(user.getEmail())) {
                String newEmail = user.getEmail();
                UUID token = UUID.randomUUID();
                currentUser.setToken(token.toString());
                mailService.sendMailForUserVerify(newEmail, currentUser.getToken());
                currentUser.setEmail(newEmail);
                currentUser.setEnable(false);
                saveUser(currentUser);
                return false;
            }
        } catch (IOException e) {
            throw new RuntimeException("File upload error: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("User update error: " + e.getMessage(), e);
        }
        saveUser(currentUser);
        return true;
    }

    @Override
    public void editUser(User user) {
        userRepository.save(user);
    }

    @Override
    public Page<UserDto> getAllUsers(PageRequest pageRequest) {
        Page<User> users = userRepository.findAllByRole(Role.USER, pageRequest);
        return users.map(userMapper::toDto);
    }

    @Override
    public Page<UserDto> getAllUsersByEmail(PageRequest pageRequest, String keyword) {
        Page<User> users = userRepository.findUserByRoleAndEmailContaining(Role.USER, keyword, pageRequest);
        return users.map(userMapper::toDto);
    }


    public boolean isValidImage(MultipartFile image) {
        String contentType = image.getContentType();
        return contentType != null && (contentType.equals("image/png") || contentType.equals("image/jpeg"));
    }

    @Override
    public List<WorkerResponse> workersByCompanyId(int companyId) {
        List<User> users = userRepository.findUserByCompany_Id(companyId);
        return userMapper.toDto(users);
    }

    @Override
    public WorkerResponse getWorkerById(int id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return userMapper.toWorker(user.get());
        }
        System.out.println(id);
        throw new EntityNotFoundException("Worker does not exist");
    }

    @Transactional
    @Override
    public void delete(User user, String password, String confirmPassword) {
        if (!(password.equals(confirmPassword) && passwordEncoder.matches(password, user.getPassword()))) {
            throw new PasswordIncorrectException("Password does not match");
        }
        if (user.getRole().equals(Role.DIRECTOR)) {
            if (companyRepository.existsCompanyByDirector(user)) {
                List<User> workers = userRepository.findUserByCompany_Id(user.getCompany().getId());
                user.setCompany(null);
                for (User worker : workers) {
                    worker.setCompany(null);
                    worker.setRole(Role.USER);
                    userRepository.save(worker);
                }
                companyRepository.deleteByDirector(user);
            }
        }
        if (user.getRole().equals(Role.WORKER)) {
            user.setRole(Role.USER);
            user.setCompany(null);
            userRepository.save(user);
            workGraphicService.deleteWorkGraphic(user.getId());
        }
        user.setPictureName("user-removal-linear-style-icon-600nw-2473043843.webp");
        user.setEnable(false);
        userRepository.save(user);
    }

    @Override
    public List<WorkerResponse> getWorkersByDirectorId(int directorId) {
        return userMapper.toDto(userRepository.getUserByCompany_Director_Id(directorId));
    }

    @Override
    public List<Integer> analyticUsers() {
        List<Integer> usersInMonths = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            usersInMonths.add(userRepository.usersInMonths(i));
        }
        return usersInMonths;
    }

    @Override
    public List<Integer> getAllRolesUsersCount() {
        List<Integer> users = new ArrayList<>();
        users.add(userRepository.countWorker());
        users.add(userRepository.countDirector());
        users.add(userRepository.countUsers());
        return users;
    }

    @Override
    public int grtRoleChangeRequestCount(User user) {
        return promotionRequestsRepository.countPromotionRequestsByRequester(user);
    }

    @Override
    public UserAuthResponse login(UserAuthRequest userAuthRequest) {
        Optional<User> userInDb = userRepository.findByEmail(userAuthRequest.getEmail());
        if (userInDb.isPresent()) {
            User user = userInDb.get();
            if (!user.isEnable()) {
                throw new AccountNotVerifiedException("User does not have verification required");
            }
            if (!passwordEncoder.matches(userAuthRequest.getPassword(), user.getPassword())) {
                throw new PasswordIncorrectException("Invalid password for user " + userAuthRequest.getEmail());
            }
            return userMapper.loginResponse(user);
        }
        throw new UserNotFoundException("User does not exist");
    }


}

