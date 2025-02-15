package org.example.gobooking.mapper;

import org.example.gobooking.dto.work.CreateServiceRequest;
import org.example.gobooking.dto.work.ServiceResponse;
import org.example.gobooking.entity.work.Service;
import org.example.gobooking.service.UserService;
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

}