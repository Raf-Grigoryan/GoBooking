package org.example.gobookingcommon.dto.work;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ServiceResponse {
    private int id;
    private String title;
    private String description;
    private double price;
    private String pictureName;
    private int duration;
}
