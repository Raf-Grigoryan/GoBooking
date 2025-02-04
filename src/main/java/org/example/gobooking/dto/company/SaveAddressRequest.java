package org.example.gobooking.dto.company;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaveAddressRequest {

    private int countryId;

    @NotBlank(message = "Region is required")
    @Size(max = 255, message = "Region must be less than 255 characters")
    private String region;

    @NotBlank(message = "City is required")
    @Size(max = 255, message = "City must be less than 255 characters")
    private String city;

    @NotBlank(message = "Street is required")
    @Size(max = 255, message = "Street must be less than 255 characters")
    private String street;

    @Size(max = 50, message = "Apartment number must be less than 50 characters")
    private String apartmentNumber;
}
