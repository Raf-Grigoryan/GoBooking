package org.example.gobooking.mapper;

import org.example.gobooking.dto.company.CompanyDto;
import org.example.gobooking.dto.company.SaveCompanyRequest;
import org.example.gobooking.entity.company.Company;
import org.example.gobooking.service.UserService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring",
        uses = {UserService.class},
        imports = {UserService.class}
)
public interface CompanyMapper {

    CompanyDto toDto(Company company);

    @Mapping(source = "directorId", target = "director")
    Company toEntity(SaveCompanyRequest request);


}