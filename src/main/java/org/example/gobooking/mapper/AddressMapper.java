package org.example.gobooking.mapper;

import org.example.gobooking.dto.company.AddressDto;
import org.example.gobooking.dto.company.SaveAddressRequest;
import org.example.gobooking.entity.company.Address;
import org.example.gobooking.service.CountryService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring",
        uses = {CountryService.class},
        imports = {CountryService.class}
)
public interface AddressMapper {

    @Mapping(source = "countryId", target = "country")
    Address toEntity(SaveAddressRequest request);

    AddressDto toDto(Address address);



}
