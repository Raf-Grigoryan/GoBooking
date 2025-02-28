package org.example.gobookingcommon.mapper;

import org.example.gobookingcommon.dto.company.CompanyDto;
import org.example.gobookingcommon.dto.company.CompanyForAdminDto;
import org.example.gobookingcommon.dto.company.CompanyResponse;
import org.example.gobookingcommon.dto.company.SaveCompanyRequest;
import org.example.gobookingcommon.entity.company.Company;
import org.example.gobookingcommon.service.UserService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring",
        uses = {UserService.class, AddressMapper.class},
        imports = {UserService.class, AddressMapper.class}
)
public interface CompanyMapper {

    CompanyDto toDto(Company company);

    CompanyResponse toResponse(Company company);

    @Mapping(source = "directorId", target = "director")
    Company toEntity(SaveCompanyRequest request);

    CompanyForAdminDto toAdminDto(Company company);


}