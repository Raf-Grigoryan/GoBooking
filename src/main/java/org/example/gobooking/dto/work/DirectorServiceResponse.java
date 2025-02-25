package org.example.gobooking.dto.work;

import lombok.Builder;
import lombok.Data;
import org.example.gobooking.dto.user.WorkerResponse;

@Data
@Builder
public class DirectorServiceResponse {

    private String title;
    private double price;
    private String pictureName;
    private int duration;
    private WorkerResponse worker;
}
