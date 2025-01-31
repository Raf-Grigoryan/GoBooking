package org.example.gobooking.service.impl;

import lombok.RequiredArgsConstructor;

import org.example.gobooking.customException.CannotVerifyUserException;
import org.example.gobooking.customException.UserOnlyExistException;
import org.example.gobooking.dto.user.SaveUserRequest;
import org.example.gobooking.entity.user.Role;
import org.example.gobooking.entity.user.User;
import org.example.gobooking.repository.UserRepository;
import org.example.gobooking.service.MailService;
import org.example.gobooking.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final MailService mailService;

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
                .role(Role.CLIENT)
                .createdAt(new Date())
                .token(token.toString())
                .build());
        mailService.sendMailForUserVerify(saveUserRequest.getEmail(), token.toString());
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

        userRepository.save(user);
    }


    @Override
    public List<String> getAdminsEmails() {
        List<String> adminEmails = new ArrayList<>();
        List <User> adminsDb = userRepository.getUserByRole((Role.ADMIN));
        for (User admin : adminsDb) {
            adminEmails.add(admin.getEmail());
        }
        return adminEmails;
    }

    @Override
    public User getUserById(int id) {
        Optional<User> userDb = userRepository.findById(id);
        if (userDb.isPresent()) {
            return userDb.get();
        }else {
            throw new CannotVerifyUserException("Error: Cannot find user with id " + id);
        }
    }



    @Override
    public User getUserById(int id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }
}
