package org.example.gobookingcommon.service.impl;


import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.gobookingcommon.customException.UnauthorizedServiceDeletionException;
import org.example.gobookingcommon.dto.work.CreateServiceRequest;
import org.example.gobookingcommon.dto.work.DirectorServiceResponse;
import org.example.gobookingcommon.dto.work.EditServiceRequest;
import org.example.gobookingcommon.dto.work.ServiceResponse;
import org.example.gobookingcommon.entity.user.User;
import org.example.gobookingcommon.entity.work.Service;
import org.example.gobookingcommon.mapper.ServiceMapper;
import org.example.gobookingcommon.repository.ServiceRepository;
import org.example.gobookingcommon.service.UserService;
import org.example.gobookingcommon.service.WorkService;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class WorkServiceImpl implements WorkService {

    private final ServiceRepository serviceRepository;

    private final ServiceMapper serviceMapper;

    private final UserService userService;

    @Value("${image.upload.path}")
    private String imageUploadPath;

    @Override
    public List<ServiceResponse> getServicesByWorkerId(int workerId) {

        return serviceMapper.mapServices(serviceRepository.findAllByWorker_id(workerId));
    }

    @Override
    public void save(CreateServiceRequest createServiceRequest, MultipartFile image) {

        if (image != null && !image.isEmpty()) {
            if (isValidImage(image)) {
                throw new IllegalArgumentException("Invalid image format");
            }

            try {
                String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
                File file = new File(imageUploadPath, fileName);
                image.transferTo(file);
                createServiceRequest.setPictureName(fileName);
            } catch (IOException e) {
                throw new RuntimeException("Failed to save image", e);
            }
        }

        serviceRepository.save(serviceMapper.mapServiceToService(createServiceRequest));
    }

    @Override
    public void deleteById(int userId, int id) {
        Service service = serviceRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        if (service.getWorker().getId() == userId) {
            serviceRepository.deleteById(id);
        } else {
            throw new UnauthorizedServiceDeletionException("You are not authorized to delete another service");
        }
    }


    @Override
    public ServiceResponse getById(int id) {
        Optional<Service> serviceInDb = serviceRepository.findById(id);
        if (serviceInDb.isPresent()) {
            return serviceMapper.mapService(serviceInDb.get());
        }
        throw new EntityNotFoundException("Service with id " + id + " not found");
    }

    @Override
    public void editService(EditServiceRequest editServiceRequest, MultipartFile image) {
        Optional<Service> serviceInDb = serviceRepository.findById(editServiceRequest.getId());
        if (serviceInDb.isEmpty()) {
            throw new EntityNotFoundException("Service with id " + editServiceRequest.getId() + " not found");
        }

        Service service = serviceInDb.get();
        if (service.getWorker().getId() != editServiceRequest.getWorkerId()) {
            throw new EntityNotFoundException("You are not allowed to edit this service");
        }

        if (image != null && !image.isEmpty()) {
            if (!isValidImage(image)) {
                throw new IllegalArgumentException("Invalid image format");
            }

            String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
            File file = new File(imageUploadPath, fileName);
            try {
                image.transferTo(file);
                editServiceRequest.setPictureName(fileName);
            } catch (IOException e) {
                throw new IllegalArgumentException("Failed to save image", e);
            }
        } else {
            editServiceRequest.setPictureName(service.getPictureName());
        }

        User worker = userService.getUserById(editServiceRequest.getWorkerId());
        if (worker == null) {
            throw new EntityNotFoundException("Worker not found");
        }

        service.setTitle(editServiceRequest.getTitle());
        service.setDescription(editServiceRequest.getDescription());
        service.setPrice(Double.parseDouble(editServiceRequest.getPrice()));
        service.setDuration(editServiceRequest.getDuration());
        service.setPictureName(editServiceRequest.getPictureName());
        service.setWorker(worker);
        serviceRepository.save(service);
    }

    private boolean isValidImage(MultipartFile image) {
        String contentType = image.getContentType();
        return contentType == null || (!contentType.equals("image/png") && !contentType.equals("image/jpeg"));
    }

    @Override
    @Named("getServiceById")
    public Service getServiceByIdForBooking(int id) {
        Optional<Service> serviceInDb = serviceRepository.findById(id);
        if (serviceInDb.isPresent()) {
            return serviceInDb.get();
        }
        throw new EntityNotFoundException("Service with id " + id + " not found");
    }

    @Override
    public int getWorkerServiceCountByDirector(int directorId) {
        return serviceRepository.countByWorker_Company_Director_Id(directorId);
    }

    @Override
    public List<ServiceResponse> getServicesByCompanyId(int companyId) {
        return serviceMapper.mapServices(serviceRepository.findRandomServicesByCompanyId(companyId));
    }

    @Override
    public List<DirectorServiceResponse> getAllServicesByDirectorId(int directorId) {
        return serviceMapper.mapDirectorServices(serviceRepository.findAllServicesByDirectorIdDesc(directorId));
    }
}

