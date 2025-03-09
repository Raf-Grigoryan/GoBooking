package org.example.gobookingcommon.dto.company;

import lombok.Data;
import org.example.gobookingcommon.entity.company.Address;
import org.example.gobookingcommon.entity.user.User;

@Data
public class CompanyForAdminDto {
    private int id;
    private String name;
    private String phone;
    private User director;
    private Address address;
    private String companyPicture;
}
