package org.example.security;

import lombok.RequiredArgsConstructor;
import org.example.gobookingcommon.entity.user.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    private final org.example.gobookingcommon.service.UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> byEmail = userService.findByEmail(username);
        if (byEmail.isPresent()) {
            User user = byEmail.get();
            return new CurrentUser(user);
        }
        throw new UsernameNotFoundException("User not found");
    }
}
