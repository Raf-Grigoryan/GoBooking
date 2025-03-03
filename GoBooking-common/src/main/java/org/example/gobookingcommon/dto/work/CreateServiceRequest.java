package org.example.gobookingcommon.dto.work;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateServiceRequest {

    private int workerId;

    @NotBlank(message = "Title cannot be empty")
    private String title;

    @NotBlank(message = "Description cannot be empty")
    private String description;

    @NotBlank(message = "Price cannot be empty")
    private String price;

    @Min(value = 30, message = "Duration must be at least 30 minute")
    private int duration;

    private String pictureName;
}
