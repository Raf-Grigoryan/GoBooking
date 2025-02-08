package org.example.gobooking.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveRoleChangeRequest {
    private int employeeId;
    private int companyId;
}
