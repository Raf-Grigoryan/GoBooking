package org.example.gobookingcommon.dto.company;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.gobookingcommon.entity.company.Country;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressDto {
    private Country country;
    private String region;
    private String city;
    private String street;
    private String apartmentNumber;
}
