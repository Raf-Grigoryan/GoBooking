package org.example.gobookingcommon.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.gobookingcommon.entity.company.Company;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleChangeRequestDto {
    Company company;
}
