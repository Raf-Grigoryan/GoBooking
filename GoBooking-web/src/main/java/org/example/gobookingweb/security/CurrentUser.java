package org.example.gobookingweb.security;

import lombok.Getter;
import org.example.gobookingcommon.entity.user.User;
import org.springframework.security.core.authority.AuthorityUtils;

@Getter
public class CurrentUser extends org.springframework.security.core.userdetails.User {

    private final User user;

    public CurrentUser(User user) {
        super(user.getName(), user.getPassword(), user.isEnable(), true, true, user.isAccountNonLocked(), AuthorityUtils.createAuthorityList(user.getRole().name()));
        this.user = user;
    }

}