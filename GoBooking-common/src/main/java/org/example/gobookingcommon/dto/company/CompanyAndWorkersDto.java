package org.example.gobookingcommon.dto.company;

import lombok.Builder;
import lombok.Data;
import org.example.gobookingcommon.dto.user.WorkerResponse;
import org.example.gobookingcommon.entity.company.Company;

import java.util.List;

@Builder
@Data
public class CompanyAndWorkersDto {
    Company company;
    List<WorkerResponse> workers;
}
