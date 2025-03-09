package org.example.gobookingcommon.dto.work;

import lombok.Builder;
import lombok.Data;
import org.example.gobookingcommon.dto.user.WorkerResponse;

import java.util.List;

@Data
@Builder
public class WorkerAndServicesDto {

    WorkerResponse worker;
    List<ServiceResponse> services;

}
