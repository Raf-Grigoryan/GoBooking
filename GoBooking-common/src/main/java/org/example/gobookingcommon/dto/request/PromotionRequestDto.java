package org.example.gobookingcommon.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.gobookingcommon.entity.user.User;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PromotionRequestDto {
    private User requester;
}
