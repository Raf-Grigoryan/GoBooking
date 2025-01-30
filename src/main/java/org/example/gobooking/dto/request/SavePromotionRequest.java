package org.example.gobooking.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SavePromotionRequest {
    @NotBlank
    private String context;
    int requester_id;
}
