package org.example.gobooking.service;

import org.example.gobooking.dto.work.CreateServiceRequest;
import org.example.gobooking.dto.work.EditServiceRequest;
import org.example.gobooking.dto.work.ServiceResponse;
import org.example.gobooking.entity.work.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface WorkService {

    List<ServiceResponse> getServicesByWorkerId(int workerId);

    void save(CreateServiceRequest createServiceRequest, MultipartFile image);

    void deleteById(int id);

    ServiceResponse getById(int id);

    void editService(EditServiceRequest editServiceRequest, MultipartFile image);

    Service getServiceByIdForBooking(int id);

}
