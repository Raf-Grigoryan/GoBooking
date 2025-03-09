package org.example.gobookingcommon.dto.work;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DirectorServiceResponse {

    private String title;
    private double price;
    private String pictureName;
    private int duration;
}
