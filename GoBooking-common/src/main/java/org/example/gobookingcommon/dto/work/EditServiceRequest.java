package org.example.gobookingcommon.dto.work;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditServiceRequest {

    private int id;

    private int workerId;

    @NotBlank(message = "Title cannot be empty")
    private String title;

    @NotBlank(message = "Description cannot be empty")
    private String description;

    @NotBlank(message = "Price cannot be empty")
    private String price;

    @Min(value = 1, message = "Duration must be at least 1 minute")
    private int duration;

    private String pictureName;
}
