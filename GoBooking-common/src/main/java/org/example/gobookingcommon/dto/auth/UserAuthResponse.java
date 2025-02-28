package org.example.gobookingcommon.dto.auth;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAuthResponse {

    private String token;
    private String email;
    private String name;
    private String surname;
}
