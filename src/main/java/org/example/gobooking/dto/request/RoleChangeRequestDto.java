package org.example.gobooking.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.gobooking.entity.company.Company;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleChangeRequestDto {
    Company company;
}
