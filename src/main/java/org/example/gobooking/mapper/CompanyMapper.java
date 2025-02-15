package org.example.gobooking.mapper;

import org.example.gobooking.dto.company.CompanyDto;
import org.example.gobooking.dto.company.CompanyResponse;
import org.example.gobooking.dto.company.SaveCompanyRequest;
import org.example.gobooking.entity.company.Company;
import org.example.gobooking.service.UserService;
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


}