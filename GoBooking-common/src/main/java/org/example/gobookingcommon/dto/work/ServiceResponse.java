package org.example.gobookingcommon.dto.work;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ServiceResponse {
    private int id;
    private String title;
    private String description;
    private double price;
    private String pictureName;
    private int duration;
}
