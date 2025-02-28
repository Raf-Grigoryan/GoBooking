package org.example.gobookingcommon.mapper;


import org.example.gobookingcommon.dto.company.AddressDto;
import org.example.gobookingcommon.dto.company.SaveAddressRequest;
import org.example.gobookingcommon.entity.company.Address;
import org.example.gobookingcommon.service.CountryService;
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
