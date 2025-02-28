package org.example.gobookingrest.endpoint;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.gobookingcommon.dto.auth.UserAuthRequest;
import org.example.gobookingcommon.dto.auth.UserAuthResponse;
import org.example.gobookingcommon.dto.user.SaveUserRequest;
import org.example.gobookingcommon.service.UserService;
import org.example.gobookingrest.util.JwtTokenUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthEndpoint {

    private final UserService userService;

    private final JwtTokenUtil jwtTokenUtil;

    @PostMapping("/login")
    public ResponseEntity<UserAuthResponse> login(@RequestBody @Valid UserAuthRequest userAuthRequest) {
        UserAuthResponse authResponse = userService.login(userAuthRequest);
        authResponse.setToken(jwtTokenUtil.generateToken(authResponse.getEmail()));
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/register")
    public void register(@RequestBody @Valid SaveUserRequest saveUserRequest) {
        userService.register(saveUserRequest);
    }
}
