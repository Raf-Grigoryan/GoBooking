package org.example.gobookingcommon.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserAuthRequest {

    @NotBlank
    private String email;
    @NotBlank
    private String password;
}
