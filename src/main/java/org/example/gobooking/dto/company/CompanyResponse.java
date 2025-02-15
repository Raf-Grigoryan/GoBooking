package org.example.gobooking.dto.company;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompanyResponse {

    private int id;
    private String name;
    private String companyPicture;
    private String phone;
    private AddressDto address;
}
