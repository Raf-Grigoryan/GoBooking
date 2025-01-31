package org.example.gobooking.controller;

import org.example.gobooking.entity.user.User;
import org.example.gobooking.security.CurrentUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class MyControllerAdvice {

    @ModelAttribute("currentUser")
    public User createUser(@AuthenticationPrincipal CurrentUser currentUser) {
        if (currentUser == null) {
            return null;
        }
        return currentUser.getUser();
    }
}

