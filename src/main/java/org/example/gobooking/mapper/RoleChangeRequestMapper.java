package org.example.gobooking.mapper;

import org.example.gobooking.dto.request.SaveRoleChangeRequest;
import org.example.gobooking.entity.request.RoleChangeRequest;
import org.example.gobooking.service.CompanyService;
import org.example.gobooking.service.UserService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        uses = {UserService.class, CompanyService.class},
        imports = {UserService.class, CompanyService.class})
public interface RoleChangeRequestMapper {

    @Mapping(source = "employeeId", target = "employee")
    @Mapping(source = "companyId", target = "company")
    RoleChangeRequest toEntity(SaveRoleChangeRequest saveRoleChangeRequest);

}
