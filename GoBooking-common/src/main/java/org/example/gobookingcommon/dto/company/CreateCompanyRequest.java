package org.example.gobookingcommon.dto.company;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCompanyRequest {

    private SaveCompanyRequest saveCompanyRequest;
    private SaveAddressRequest saveAddressRequest;
}
