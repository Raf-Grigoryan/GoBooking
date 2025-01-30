package org.example.gobooking.security;

import lombok.RequiredArgsConstructor;
import org.example.gobooking.entity.user.User;
import org.example.gobooking.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> byEmail = userService.findByEmail(username);
        if (byEmail.isPresent()) {
            User userFromDb = byEmail.get();
            return new CurrentUser(userFromDb);
        }
        throw new UsernameNotFoundException("User with " + username + " does not exists");
    }
}