package org.example.gobooking.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.gobooking.entity.user.User;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PromotionRequestDto {
    User requester;
}
