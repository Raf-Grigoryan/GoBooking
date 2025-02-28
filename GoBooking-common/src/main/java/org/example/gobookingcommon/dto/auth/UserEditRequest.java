package org.example.gobookingcommon.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class UserEditRequest {


    @NotBlank(message = "Name cannot be empty")
    @Size(min = 2, message = "Name must be at least 2 characters long")
    private String name;

    @NotBlank(message = "Surname cannot be empty")
    @Size(min = 2, message = "Surname must be at least 2 characters long")
    private String surname;

    @NotBlank(message = "Phone cannot be empty")
    private String phone;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email address")
    private String email;

    private String pictureName;
}
