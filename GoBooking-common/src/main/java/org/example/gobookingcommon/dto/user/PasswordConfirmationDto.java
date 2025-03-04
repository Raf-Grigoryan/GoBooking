package org.example.gobookingcommon.dto.user;

import lombok.Data;

@Data
public class PasswordConfirmationDto {
    private String password;
    private String confirmPassword;
}
