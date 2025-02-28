package org.example.gobookingcommon.dto.company;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaveCompanyRequest {

    @NotBlank(message = "Company name is required")
    @Size(max = 255, message = "Company name must be less than 255 characters")
    private String name;

    @NotBlank(message = "Phone cannot be empty")
    private String phone;

    @Min(value = 1, message = "Director ID must be a positive integer")
    private int directorId;
}
