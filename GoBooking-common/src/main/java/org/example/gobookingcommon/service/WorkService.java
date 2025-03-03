package org.example.gobookingcommon.service;


import org.example.gobookingcommon.dto.work.CreateServiceRequest;
import org.example.gobookingcommon.dto.work.DirectorServiceResponse;
import org.example.gobookingcommon.dto.work.EditServiceRequest;
import org.example.gobookingcommon.dto.work.ServiceResponse;
import org.example.gobookingcommon.entity.work.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface WorkService {

    List<ServiceResponse> getServicesByWorkerId(int workerId);

    void save(CreateServiceRequest createServiceRequest, MultipartFile image);

    void deleteById(int userId,int id);

    ServiceResponse getById(int id);

    void editService(EditServiceRequest editServiceRequest, MultipartFile image);

    Service getServiceByIdForBooking(int id);

    int getWorkerServiceCountByDirector(int directorId);

    List<ServiceResponse> getServicesByCompanyId(int companyId);

    List<DirectorServiceResponse> getAllServicesByDirectorId(int directorId);
}
