package org.example.gobookingcommon.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.example.gobookingcommon.customException.UnauthorizedServiceDeletionException;
import org.example.gobookingcommon.dto.work.CreateServiceRequest;
import org.example.gobookingcommon.dto.work.DirectorServiceResponse;
import org.example.gobookingcommon.dto.work.EditServiceRequest;
import org.example.gobookingcommon.dto.work.ServiceResponse;
import org.example.gobookingcommon.entity.user.Role;
import org.example.gobookingcommon.entity.user.User;
import org.example.gobookingcommon.entity.work.Service;
import org.example.gobookingcommon.mapper.ServiceMapper;
import org.example.gobookingcommon.repository.ServiceRepository;
import org.example.gobookingcommon.service.WorkService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class WorkServiceImplTest {

    @Mock
    private ServiceRepository serviceRepository;


    @InjectMocks
    private WorkServiceImpl workService;

    @Mock
    private ServiceMapper serviceMapper;

    private final String imageUploadPath = "/fake/path/";

    @Mock
    private UserServiceImpl userService;


    @Test
    void getServicesByWorkerId_Success() {
        int workerId = 1;

        Service service1 = new Service();
        service1.setId(1);
        Service service2 = new Service();
        service2.setId(2);

        List<Service> serviceList = List.of(service1, service2);

        when(serviceRepository.findAllByWorker_id(workerId)).thenReturn(serviceList);

        ServiceResponse serviceResponse1 = new ServiceResponse();
        ServiceResponse serviceResponse2 = new ServiceResponse();
        when(serviceMapper.mapServices(serviceList)).thenReturn(List.of(serviceResponse1, serviceResponse2));

        List<ServiceResponse> result = workService.getServicesByWorkerId(workerId);

        verify(serviceRepository, times(1)).findAllByWorker_id(workerId);
        verify(serviceMapper, times(1)).mapServices(serviceList);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertSame(serviceResponse1, result.get(0));
        assertSame(serviceResponse2, result.get(1));
    }


    @Test
    void save_ServiceWithoutImage_Success() {
        CreateServiceRequest createServiceRequest = new CreateServiceRequest();
        createServiceRequest.setWorkerId(1);
        createServiceRequest.setTitle("Test Service");
        createServiceRequest.setDescription("Test Description");
        createServiceRequest.setPrice("100");
        createServiceRequest.setDuration(60);

        when(serviceMapper.mapServiceToService(createServiceRequest)).thenReturn(new Service());

        workService.save(createServiceRequest, null);

        verify(serviceRepository, times(1)).save(any(Service.class));
    }


    @Test
    void deleteById_ServiceExists_Authorized() {
        User worker = new User();
        worker.setId(1);

        Service service = new Service();
        service.setId(1);
        service.setWorker(worker);

        when(serviceRepository.findById(1)).thenReturn(Optional.of(service));

        workService.deleteById(1, 1);

        verify(serviceRepository, times(1)).deleteById(1);
    }

    @Test
    void deleteById_ServiceExists_Unauthorized() {
        User worker = new User();
        worker.setId(1);

        Service service = new Service();
        service.setId(1);
        service.setWorker(worker);

        when(serviceRepository.findById(1)).thenReturn(Optional.of(service));

        UnauthorizedServiceDeletionException thrown = assertThrows(UnauthorizedServiceDeletionException.class, () ->
                workService.deleteById(2, 1));
        assertEquals("You are not authorized to delete another service", thrown.getMessage());
    }


    @Test
    void getWorkServiceByIdForBooking_ServiceFound() {
        int serviceId = 1;
        Service service = new Service();
        service.setId(serviceId);

        when(serviceRepository.findById(serviceId)).thenReturn(Optional.of(service));

        Service result = workService.getServiceByIdForBooking(serviceId);
        assertNotNull(result);
        assertEquals(serviceId, result.getId());
    }

    @Test
    void getWorkServiceByIdForBooking_ServiceNotFound() {

        int serviceId = 1;
        when(serviceRepository.findById(serviceId)).thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class,
                () -> workService.getServiceByIdForBooking(serviceId));

        assertTrue(thrown.getMessage().contains("Service with id"));
    }

    @Test
    void getById_ServiceFound() {
        int serviceId = 1;
        Service service = new Service();
        service.setId(serviceId);
        ServiceResponse serviceResponse = new ServiceResponse();
        serviceResponse.setId(serviceId);

        when(serviceRepository.findById(serviceId)).thenReturn(Optional.of(service));
        when(serviceMapper.mapService(service)).thenReturn(serviceResponse);

        ServiceResponse result = workService.getById(serviceId);

        assertNotNull(result);
        assertEquals(serviceId, result.getId());
    }

    @Test
    void getById_ServiceNotFound() {
        int serviceId = 1;

        when(serviceRepository.findById(serviceId)).thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class,
                () -> workService.getById(serviceId));
        assertTrue(thrown.getMessage().contains("Service with id"));
    }

    @Test
    void getServiceByIdForBooking_ServiceFound() {
        int serviceId = 1;
        Service service = new Service();
        service.setId(serviceId);
        when(serviceRepository.findById(serviceId)).thenReturn(Optional.of(service));
        Service result = workService.getServiceByIdForBooking(serviceId);
        assertNotNull(result);
        assertEquals(serviceId, result.getId());
    }

    @Test
    void getServiceByIdForBooking_ServiceNotFound() {
        int serviceId = 1;

        when(serviceRepository.findById(serviceId)).thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class,
                () -> workService.getServiceByIdForBooking(serviceId));

        assertEquals("Service with id " + serviceId + " not found", thrown.getMessage());
    }

    @Test
    void getAllServicesByDirectorId_ReturnsMappedServices() {
        int directorId = 1;

        List<Service> services = List.of(
                Service.builder()
                        .title("Service1")
                        .price(100.0)
                        .pictureName("image1.jpg")
                        .duration(60)
                        .build(),
                Service.builder()
                        .title("Service2")
                        .price(150.0)
                        .pictureName("image2.jpg")
                        .duration(90)
                        .build()
        );

        List<DirectorServiceResponse> expectedResponses = List.of(
                DirectorServiceResponse.builder()
                        .title("Service1")
                        .price(100.0)
                        .pictureName("image1.jpg")
                        .duration(60)
                        .build(),
                DirectorServiceResponse.builder()
                        .title("Service2")
                        .price(150.0)
                        .pictureName("image2.jpg")
                        .duration(90)
                        .build()
        );

        when(serviceRepository.findAllServicesByDirectorIdDesc(directorId)).thenReturn(services);
        when(serviceMapper.mapDirectorServices(services)).thenReturn(expectedResponses);

        List<DirectorServiceResponse> result = workService.getAllServicesByDirectorId(directorId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedResponses, result);
        verify(serviceRepository, times(1)).findAllServicesByDirectorIdDesc(directorId);
        verify(serviceMapper, times(1)).mapDirectorServices(services);
    }


    @Test
    void getServicesByCompanyId_ReturnsMappedServices() {
        int companyId = 1;

        List<Service> services = List.of(
                Service.builder()
                        .id(1)
                        .title("Service1")
                        .description("Description1")
                        .price(100.0)
                        .pictureName("image1.jpg")
                        .duration(60)
                        .build(),
                Service.builder()
                        .id(2)
                        .title("Service2")
                        .description("Description2")
                        .price(150.0)
                        .pictureName("image2.jpg")
                        .duration(90)
                        .build()
        );

        List<ServiceResponse> expectedResponses = List.of(
                ServiceResponse.builder()
                        .id(1)
                        .title("Service1")
                        .description("Description1")
                        .price(100.0)
                        .pictureName("image1.jpg")
                        .duration(60)
                        .build(),
                ServiceResponse.builder()
                        .id(2)
                        .title("Service2")
                        .description("Description2")
                        .price(150.0)
                        .pictureName("image2.jpg")
                        .duration(90)
                        .build()
        );

        when(serviceRepository.findRandomServicesByCompanyId(companyId)).thenReturn(services);
        when(serviceMapper.mapServices(services)).thenReturn(expectedResponses);

        List<ServiceResponse> result = workService.getServicesByCompanyId(companyId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedResponses, result);
        verify(serviceRepository, times(1)).findRandomServicesByCompanyId(companyId);
        verify(serviceMapper, times(1)).mapServices(services);
    }


    @Test
    void save_ServiceWithoutImageSuccess() {
        CreateServiceRequest createServiceRequest = CreateServiceRequest.builder()
                .title("Test Service")
                .description("Test Description")
                .price("100")
                .duration(60)
                .build();

        workService.save(createServiceRequest, null);

        assertNull(createServiceRequest.getPictureName());
    }

    @Test
    void isValidImage_ValidPngImage_ReturnsFalse() {
        MultipartFile mockImage = mock(MultipartFile.class);
        when(mockImage.getContentType()).thenReturn("image/png");

        boolean result = workService.isValidImage(mockImage);

        assertFalse(result);
    }

    @Test
    void isValidImage_ValidJpegImage_ReturnsFalse() {
        MultipartFile mockImage = mock(MultipartFile.class);
        when(mockImage.getContentType()).thenReturn("image/jpeg");

        boolean result = workService.isValidImage(mockImage);

        assertFalse(result);
    }

    @Test
    void isValidImage_InvalidImageFormat_ReturnsTrue() {
        MultipartFile mockImage = mock(MultipartFile.class);
        when(mockImage.getContentType()).thenReturn("image/gif");

        boolean result = workService.isValidImage(mockImage);

        assertTrue(result);
    }

    @Test
    void isValidImage_NullContentType_ReturnsTrue() {
        MultipartFile mockImage = mock(MultipartFile.class);
        when(mockImage.getContentType()).thenReturn(null);

        boolean result = workService.isValidImage(mockImage);

        assertTrue(result);
    }

    @Test
    void getWorkerServiceCountByDirector_ReturnsCorrectCount() {
        int directorId = 1;
        int expectedCount = 5;
        when(serviceRepository.countByWorker_Company_Director_Id_AndWorker_Role(directorId, Role.WORKER)).thenReturn(expectedCount);
        int actualCount = workService.getWorkerServiceCountByDirector(directorId);
        assertEquals(expectedCount, actualCount, "The service count should match the expected value.");
    }


    @Test
    void save_WhenNoImage_ShouldSaveService() {
        CreateServiceRequest request = new CreateServiceRequest();

        workService.save(request, null);

        verify(serviceRepository, times(1)).save(any());
    }

    @Test
    void save_WhenValidImage_ShouldSaveWithImage() {
        CreateServiceRequest request = new CreateServiceRequest();
        MockMultipartFile mockFile = new MockMultipartFile(
                "image", "test.jpg", "image/jpeg", new byte[]{1, 2, 3}
        );
        workService.save(request, mockFile);
        assertNotNull(request.getPictureName());
        assertTrue(request.getPictureName().endsWith(".jpg"));
        verify(serviceRepository, times(1)).save(any());
    }

    @Test
    void save_WhenInvalidImage_ShouldThrowException() {
        CreateServiceRequest request = new CreateServiceRequest();
        MockMultipartFile invalidFile = new MockMultipartFile(
                "image", "test.txt", "text/plain", new byte[]{1, 2, 3}
        );

        WorkService spyService = spy(workService);
        assertThrows(IllegalArgumentException.class, () -> spyService.save(request, invalidFile));
    }


    @Test
    void save_WhenIOException_ShouldThrowRuntimeException() throws Exception {
        CreateServiceRequest request = new CreateServiceRequest();
        MockMultipartFile mockFile = spy(new MockMultipartFile(
                "image", "test.jpg", "image/jpeg", new byte[]{1, 2, 3}
        ));

        doThrow(new IOException("Failed to save file")).when(mockFile).transferTo(any(File.class));

        assertThrows(RuntimeException.class, () -> workService.save(request, mockFile));
    }


    @Test
    void editService_WhenServiceNotFound_ShouldThrowEntityNotFoundException() {
        EditServiceRequest request = new EditServiceRequest();
        request.setId(20);

        when(serviceRepository.findById(20)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> workService.editService(request, null));
    }

    @Test
    void editService_WhenUserNotAllowed_ShouldThrowEntityNotFoundException() {
        EditServiceRequest request = new EditServiceRequest();
        request.setId(10);
        request.setWorkerId(20);
        User worker = new User();
        worker.setId(10);
        Service service = new Service();
        service.setWorker(worker);
        when(serviceRepository.findById(10)).thenReturn(Optional.of(service));
        assertThrows(EntityNotFoundException.class, () -> workService.editService(request, null));
    }


    @Test
    void editService_WhenValidRequestWithoutImage_ShouldEditService() {
        EditServiceRequest request = new EditServiceRequest();
        request.setId(10);
        request.setWorkerId(20);
        request.setTitle("New Title");
        request.setDescription("New Description");
        request.setPrice("100");
        request.setDuration(60);

        User worker = new User();
        worker.setId(20);

        Service service = new Service();
        service.setWorker(worker);
        service.setPictureName("old.jpg");

        when(serviceRepository.findById(10)).thenReturn(Optional.of(service));
        when(userService.getUserById(20)).thenReturn(worker);

        workService.editService(request, null);

        assertEquals("New Title", service.getTitle());
        assertEquals("New Description", service.getDescription());
        assertEquals(100.0, service.getPrice());
        assertEquals(60, service.getDuration());
        assertEquals("old.jpg", service.getPictureName());
        verify(serviceRepository, times(1)).save(service);
    }
}

