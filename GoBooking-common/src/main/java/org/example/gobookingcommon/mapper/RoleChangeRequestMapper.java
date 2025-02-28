package org.example.gobookingcommon.mapper;


import org.example.gobookingcommon.dto.request.RoleChangeRequestDto;
import org.example.gobookingcommon.dto.request.SaveRoleChangeRequest;
import org.example.gobookingcommon.entity.request.RoleChangeRequest;
import org.example.gobookingcommon.service.CompanyService;
import org.example.gobookingcommon.service.UserService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        uses = {UserService.class, CompanyService.class},
        imports = {UserService.class, CompanyService.class})
public interface RoleChangeRequestMapper {

    @Mapping(source = "employeeId", target = "employee")
    @Mapping(source = "companyId", target = "company")
    RoleChangeRequest toEntity(SaveRoleChangeRequest saveRoleChangeRequest);

    RoleChangeRequestDto toDto(RoleChangeRequest roleChangeRequest);

}
