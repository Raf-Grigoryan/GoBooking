package org.example.gobookingcommon.service.impl;

import jakarta.persistence.EntityNotFoundException;
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
import org.example.gobookingcommon.repository.UserRepository;
import org.example.gobookingcommon.service.MailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private MailService mailService;

    @Mock
    private UserMapper userMapper;


    @Test
    void shouldRegisterUserSuccessfully() {
        SaveUserRequest request = new SaveUserRequest("John", "Doe", "john.doe@example.com", "password123", "1234567890");
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        userService.register(request);
        verify(userRepository).save(any(User.class));
        verify(mailService).sendMailForUserVerify(eq(request.getEmail()), anyString());
    }

    @Test
    void shouldThrowExceptionWhenUserAlreadyExists() {
        SaveUserRequest request = new SaveUserRequest("John", "Doe", "john.doe@example.com", "password123", "1234567890");
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(new User()));
        assertThrows(UserOnlyExistException.class, () -> userService.register(request));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldSaveUserWithCorrectDetails() {
        SaveUserRequest request = new SaveUserRequest("John", "Doe", "john.doe@example.com", "password123", "1234567890");
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        userService.register(request);
    }

    @Test
    void shouldGenerateTokenAndSendVerificationEmail() {
        SaveUserRequest request = new SaveUserRequest("John", "Doe", "john.doe@example.com", "password123", "1234567890");
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        userService.register(request);
        verify(mailService).sendMailForUserVerify(eq(request.getEmail()), anyString());

    }

    @Test
    void shouldGenerateRandomUUIDAsToken() {
        SaveUserRequest request = new SaveUserRequest("John", "Doe", "john.doe@example.com", "password123", "1234567890");
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        userService.register(request);
    }

    @Test
    void shouldVerifyUserAccountSuccessfully() {
        String email = "john.doe@example.com";
        String token = "validToken";
        User user = new User();
        user.setEmail(email);
        user.setToken(token);
        user.setEnable(false);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        userService.verifyUserAccount(email, token);

        assertTrue(user.isEnable(), "User should be enabled");
        assertNull(user.getToken(), "Token should be null after verification");
        verify(userRepository).save(user);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        String email = "nonexistent@example.com";
        String token = "someToken";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        CannotVerifyUserException exception = assertThrows(CannotVerifyUserException.class,
                () -> userService.verifyUserAccount(email, token));

        assertEquals("Error: Cannot verify user account. User with email " + email + " not found.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenTokenIsInvalid() {
        String email = "john.doe@example.com";
        String token = "invalidToken";
        User user = new User();
        user.setEmail(email);
        user.setToken("differentToken");
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        CannotVerifyUserException exception = assertThrows(CannotVerifyUserException.class,
                () -> userService.verifyUserAccount(email, token));
        assertEquals("Error: Invalid verification token.", exception.getMessage());
    }

    @Test
    void shouldReturnListOfAdminEmails() {
        User admin1 = User.builder()
                .name("admin1")
                .surname("user1")
                .email("admin1@example.com")
                .password("password")
                .role(Role.ADMIN)
                .build();

        User admin2 = User.builder()
                .name("admin2")
                .surname("user2")
                .email("admin2@example.com")
                .password("password")
                .role(Role.ADMIN)
                .build();
        when(userRepository.getUserByRole(Role.ADMIN)).thenReturn(Arrays.asList(admin1, admin2));
        List<String> adminEmails = userService.getAdminsEmails();
        assertEquals(2, adminEmails.size());
        assertEquals("admin1@example.com", adminEmails.get(0));
        assertEquals("admin2@example.com", adminEmails.get(1));
    }

    @Test
    void shouldReturnUserWhenEmailExists() {
        String email = "test@example.com";
        User user = User.builder()
                .id(1)
                .name("John")
                .surname("Doe")
                .email(email)
                .password("password")
                .role(Role.USER)
                .build();
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        Optional<User> result = userService.findByEmail(email);
        assertTrue(result.isPresent());
        assertEquals(email, result.get().getEmail());
    }

    @Test
    void shouldReturnEmptyWhenEmailDoesNotExist() {
        String email = "nonexistent@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        Optional<User> result = userService.findByEmail(email);
        assertFalse(result.isPresent());
    }

    @Test
    void shouldReturnUserWhenFoundById() {
        User testUser = User.builder()
                .id(1)
                .name("John")
                .surname("Doe")
                .email("john.doe@example.com")
                .password("password")
                .role(Role.USER)
                .enable(false)
                .build();

        when(userRepository.findById(1)).thenReturn(Optional.of(testUser));
        User result = userService.getUserById(1);
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("John", result.getName());
        assertEquals("Doe", result.getSurname());
        assertEquals(Role.USER, result.getRole());
    }

    @Test
    void shouldThrowCannotVerifyUserExceptionWhenUserNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());
        CannotVerifyUserException exception = assertThrows(CannotVerifyUserException.class, () -> userService.getUserById(1));
        assertEquals("Error: Cannot find user with id 1", exception.getMessage());
    }


    @Test
    void shouldChangePasswordWhenValid() {
        User user = User.builder()
                .id(1)
                .name("John")
                .surname("Doe")
                .email("john.doe@example.com")
                .password("oldPassword")
                .role(Role.USER)
                .build();
        when(passwordEncoder.matches("oldPassword", user.getPassword())).thenReturn(true);
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");
        PasswordChangeRequest passwordChangeRequest = new PasswordChangeRequest("oldPassword", "newPassword", "newPassword");
        userService.changePassword(user, passwordChangeRequest);
        verify(userRepository, times(1)).save(user);
        assertEquals("encodedNewPassword", user.getPassword());

        verify(mailService, times(1)).sendMailForChangePassword(user.getEmail(), "newPassword");
    }

    @Test
    void shouldThrowPasswordIncorrectExceptionWhenOldPasswordDoesNotMatch() {
        User user = User.builder()
                .id(1)
                .name("John")
                .surname("Doe")
                .email("john.doe@example.com")
                .password("oldPassword")
                .role(Role.USER)
                .build();
        when(passwordEncoder.matches("wrongOldPassword", user.getPassword())).thenReturn(false);
        PasswordChangeRequest passwordChangeRequest = new PasswordChangeRequest("wrongOldPassword", "newPassword", "newPassword");

        PasswordIncorrectException exception = assertThrows(PasswordIncorrectException.class,
                () -> userService.changePassword(user, passwordChangeRequest));
        assertEquals("Error: Old password does not match", exception.getMessage());
    }

    @Test
    void shouldThrowConfirmPasswordIncorrectExceptionWhenConfirmPasswordDoesNotMatch() {
        User user = User.builder()
                .id(1)
                .name("John")
                .surname("Doe")
                .email("john.doe@example.com")
                .password("oldPassword")
                .role(Role.USER)
                .build();
        when(passwordEncoder.matches("oldPassword", user.getPassword())).thenReturn(true);
        PasswordChangeRequest passwordChangeRequest = new PasswordChangeRequest("oldPassword", "newPassword", "differentConfirmPassword");
        ConfirmPasswordIncorrectException exception = assertThrows(ConfirmPasswordIncorrectException.class,
                () -> userService.changePassword(user, passwordChangeRequest));
        assertEquals("Error: Confirm password does not match", exception.getMessage());
    }


    @Test
    void shouldReturnFalseIfEmailIsChangedAndVerificationSent() {

        User currentUser = User.builder()
                .id(1)
                .name("John")
                .surname("Doe")
                .email("john.doe@example.com")
                .phone("123456789")
                .enable(true)
                .build();

        UserEditRequest userEditRequest = UserEditRequest.builder()
                .name("newName")
                .surname("newSurname")
                .phone("987654321")
                .email("newEmail@example.com")
                .build();

        MultipartFile image = new MockMultipartFile("file", "image.jpg", "image/jpeg", new byte[0]);

        boolean result = userService.editUser(currentUser, userEditRequest, image);

        assertFalse(result);
        assertEquals("newEmail@example.com", currentUser.getEmail());
        assertEquals("newName", currentUser.getName());
        assertFalse(currentUser.isEnable());
        verify(mailService, times(1)).sendMailForUserVerify(eq("newEmail@example.com"), anyString());
    }

    @Test
    void shouldNotChangeEmailIfNoEmailChange() {
        User currentUser = User.builder()
                .id(1)
                .name("John")
                .surname("Doe")
                .email("john.doe@example.com")
                .phone("123456789")
                .enable(true)
                .build();

        UserEditRequest userEditRequest = UserEditRequest.builder()
                .name("newName")
                .surname("newSurname")
                .phone("987654321")
                .email("john.doe@example.com")
                .build();

        MultipartFile image = new MockMultipartFile("file", "image.jpg", "image/jpeg", new byte[0]);

        boolean result = userService.editUser(currentUser, userEditRequest, image);

        assertTrue(result);
        assertEquals("john.doe@example.com", currentUser.getEmail());
        verify(mailService, times(0)).sendMailForUserVerify(anyString(), anyString());
    }

    @Test
    void shouldReturnPageOfUserDto() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        User user = User.builder()
                .id(1)
                .name("John")
                .surname("Doe")
                .email("john.doe@example.com")
                .phone("123456789")
                .enable(true)
                .build();
        UserDto userDto = UserDto.builder()
                .id(1)
                .email("john.doe@example.com")
                .pictureName("profilePic.jpg")
                .build();
        Page<User> userPage = new PageImpl<>(List.of(user));
        when(userRepository.findAllByRole(Role.USER, pageRequest)).thenReturn(userPage);
        when(userMapper.toDto(user)).thenReturn(userDto);
        Page<UserDto> result = userService.getAllUsers(pageRequest);
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(userDto, result.getContent().get(0));
        verify(userRepository, times(1)).findAllByRole(Role.USER, pageRequest);
        verify(userMapper, times(1)).toDto(user);
    }

    @Test
    void testLogin_UserNotFound() {
        UserAuthRequest validRequest = new UserAuthRequest();
        validRequest.setEmail("test@example.com");
        validRequest.setPassword("password123");

        when(userRepository.findByEmail(validRequest.getEmail())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.login(validRequest));
    }

    @Test
    void testLogin_UserNotVerified() {
        UserAuthRequest validRequest = new UserAuthRequest();
        validRequest.setEmail("test@example.com");
        validRequest.setPassword("password123");

        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword123");
        user.setEnable(false);

        when(userRepository.findByEmail(validRequest.getEmail())).thenReturn(Optional.of(user));

        assertThrows(AccountNotVerifiedException.class, () -> userService.login(validRequest));
    }

    @Test
    void testLogin_PasswordIncorrect() {
        UserAuthRequest validRequest = new UserAuthRequest();
        validRequest.setEmail("test@example.com");
        validRequest.setPassword("password123");

        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword123");
        user.setEnable(true);

        when(userRepository.findByEmail(validRequest.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(validRequest.getPassword(), user.getPassword())).thenReturn(false);

        assertThrows(PasswordIncorrectException.class, () -> userService.login(validRequest));
    }

    @Test
    void testLogin_Success() {
        UserAuthRequest validRequest = new UserAuthRequest();
        validRequest.setEmail("test@example.com");
        validRequest.setPassword("password123");

        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword123");
        user.setEnable(true);
        UserAuthResponse userAuthResponse = new UserAuthResponse();
        when(userRepository.findByEmail(validRequest.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(validRequest.getPassword(), user.getPassword())).thenReturn(true);
        when(userMapper.loginResponse(user)).thenReturn(userAuthResponse);

        UserAuthResponse result = userService.login(validRequest);

        assertNotNull(result);
        assertEquals(userAuthResponse, result);
    }

    @Test
    void testGetAllRolesUsersCount() {
        when(userRepository.countWorker()).thenReturn(10);
        when(userRepository.countDirector()).thenReturn(5);
        when(userRepository.countUsers()).thenReturn(100);
        List<Integer> result = userService.getAllRolesUsersCount();

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(10, result.get(0));
        assertEquals(5, result.get(1));
        assertEquals(100, result.get(2));

        verify(userRepository, times(1)).countWorker();
        verify(userRepository, times(1)).countDirector();
        verify(userRepository, times(1)).countUsers();
    }

    @Test
    void testWorkersByCompanyId() {
        int companyId = 1;
        User worker1 = new User();
        worker1.setId(1);
        worker1.setRole(Role.WORKER);

        User worker2 = new User();
        worker2.setId(2);
        worker2.setRole(Role.WORKER);

        List<User> workers = List.of(worker1, worker2);

        WorkerResponse workerResponse1 = new WorkerResponse();
        workerResponse1.setId(worker1.getId());

        WorkerResponse workerResponse2 = new WorkerResponse();
        workerResponse2.setId(worker2.getId());

        List<WorkerResponse> workerResponses = List.of(workerResponse1, workerResponse2);

        when(userRepository.findUserByCompany_Id(companyId)).thenReturn(workers);
        when(userMapper.toDto(workers)).thenReturn(workerResponses);

        List<WorkerResponse> result = userService.workersByCompanyId(companyId);

        assertEquals(2, result.size());
        assertEquals(workerResponse1.getId(), result.get(0).getId());
        assertEquals(workerResponse2.getId(), result.get(1).getId());

        verify(userRepository, times(1)).findUserByCompany_Id(companyId);
        verify(userMapper, times(1)).toDto(workers);
    }

    @Test
    void testAnalyticUsers() {
        List<Integer> expectedUsersInMonths = List.of(10, 15, 20, 5, 30, 25, 40, 50, 60, 70, 80, 90);
        for (int i = 1; i <= 12; i++) {
            when(userRepository.usersInMonths(i)).thenReturn(expectedUsersInMonths.get(i - 1));
        }
        List<Integer> result = userService.analyticUsers();
        assertEquals(expectedUsersInMonths, result);
        for (int i = 1; i <= 12; i++) {
            verify(userRepository, times(1)).usersInMonths(i);
        }
    }

    @Test
    void testGetWorkersByCompanyId() {
        int companyId = 1;

        User user1 = new User();
        user1.setId(1);
        user1.setEmail("worker1@example.com");

        User user2 = new User();
        user2.setId(2);
        user2.setEmail("worker2@example.com");

        List<User> users = List.of(user1, user2);

        when(userRepository.findUserByCompany_Id(companyId)).thenReturn(users);

        WorkerResponse workerResponse1 = WorkerResponse.builder()
                .id(1)
                .name("worker1")
                .surname("surname1")
                .phone("123456789")
                .pictureName("picture1.jpg")
                .build();

        WorkerResponse workerResponse2 = WorkerResponse.builder()
                .id(2)
                .name("worker2")
                .surname("surname2")
                .phone("987654321")
                .pictureName("picture2.jpg")
                .build();

        when(userMapper.toDto(users)).thenReturn(List.of(workerResponse1, workerResponse2));

        List<WorkerResponse> result = userService.workersByCompanyId(companyId);

        assertEquals(2, result.size());
        assertEquals(workerResponse1, result.get(0));
        assertEquals(workerResponse2, result.get(1));

        verify(userRepository, times(1)).findUserByCompany_Id(companyId);
        verify(userMapper, times(1)).toDto(users);
    }

    @Test
    void testIsValidImage_ValidImage() {
        MultipartFile image = mock(MultipartFile.class);
        when(image.getContentType()).thenReturn("image/png");
        assertTrue(userService.isValidImage(image));
    }

    @Test
    void testIsValidImage_InvalidImage() {
        // Mock an invalid image with content type "text/plain"
        MultipartFile image = mock(MultipartFile.class);
        when(image.getContentType()).thenReturn("text/plain");

        // Assert that the image is not valid
        assertFalse(userService.isValidImage(image));
    }

    @Test
    void testIsValidImage_NullContentType() {
        MultipartFile image = mock(MultipartFile.class);
        when(image.getContentType()).thenReturn(null);
        assertFalse(userService.isValidImage(image));
    }

    @Test
    void testIsValidImage_JPEGImage() {
        MultipartFile image = mock(MultipartFile.class);
        when(image.getContentType()).thenReturn("image/jpeg");
        assertTrue(userService.isValidImage(image));
    }

    @Test
    void testGetWorkerById_WorkerExists() {

        int workerId = 1;
        User mockUser = new User();
        mockUser.setId(workerId);
        mockUser.setEmail("worker@example.com");

        WorkerResponse mockWorkerResponse = WorkerResponse.builder()
                .id(workerId)
                .name("worker")
                .surname("example")
                .phone("123456789")
                .pictureName("profile_picture.png")
                .build();
        when(userRepository.findById(workerId)).thenReturn(Optional.of(mockUser));
        when(userMapper.toWorker(mockUser)).thenReturn(mockWorkerResponse);
        WorkerResponse result = userService.getWorkerById(workerId);
        assertEquals(mockWorkerResponse, result);
        verify(userRepository, times(1)).findById(workerId);
        verify(userMapper, times(1)).toWorker(mockUser);
    }

    @Test
    void testGetWorkerById_WorkerNotFound() {
        int workerId = 1;

        when(userRepository.findById(workerId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> userService.getWorkerById(workerId));

        assertEquals("Worker does not exist", exception.getMessage());
        verify(userRepository, times(1)).findById(workerId);
        verify(userMapper, times(0)).toWorker(Mockito.any());
    }


}