package org.example.gobookingcommon.mapper;


import org.example.gobookingcommon.dto.work.CreateServiceRequest;
import org.example.gobookingcommon.dto.work.DirectorServiceResponse;
import org.example.gobookingcommon.dto.work.ServiceResponse;
import org.example.gobookingcommon.entity.work.Service;
import org.example.gobookingcommon.service.UserService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = {UserService.class},
        imports = {UserService.class})
public interface ServiceMapper {

    ServiceResponse mapService(Service service);

    @Mapping(source = "workerId", target = "worker")
    Service mapServiceToService(CreateServiceRequest serviceRequest);

    List<ServiceResponse> mapServices(List<Service> services);

    @Mapping(source = "workerId", target = "worker")
    List<DirectorServiceResponse> mapDirectorServices(List<Service> services);

}