package org.example.gobookingweb.controller;


import org.example.gobookingcommon.entity.user.User;
import org.example.gobookingweb.security.CurrentUser;
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


