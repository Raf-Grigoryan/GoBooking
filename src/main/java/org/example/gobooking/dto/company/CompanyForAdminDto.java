package org.example.gobooking.dto.company;

import lombok.Data;
import org.example.gobooking.entity.company.Address;
import org.example.gobooking.entity.user.User;

@Data
public class CompanyForAdminDto {
    private int id;
    private String name;
    private String phone;
    private User director;
    private Address address;
    private String companyPicture;
}
