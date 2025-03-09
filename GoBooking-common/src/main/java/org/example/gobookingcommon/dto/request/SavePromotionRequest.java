package org.example.gobookingcommon.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SavePromotionRequest {
    @NotBlank
    private String context;
    private int requester_id;
}
