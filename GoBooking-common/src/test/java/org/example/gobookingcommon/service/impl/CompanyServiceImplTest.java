package org.example.gobookingcommon.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.example.gobookingcommon.customException.AddressOnlyExistException;
import org.example.gobookingcommon.customException.CompanyAlreadyExistsException;
import org.example.gobookingcommon.customException.CompanyNotFoundException;
import org.example.gobookingcommon.customException.DirectorNotMatchException;
import org.example.gobookingcommon.dto.company.*;
import org.example.gobookingcommon.entity.company.Company;
import org.example.gobookingcommon.entity.user.User;
import org.example.gobookingcommon.mapper.AddressMapper;
import org.example.gobookingcommon.mapper.CompanyMapper;
import org.example.gobookingcommon.repository.CompanyRepository;
import org.example.gobookingcommon.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompanyServiceTest {

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private CompanyMapper companyMapper;

    @Mock
    private AddressServiceImpl addressService;

    @Mock
    private AddressMapper addressMapper;

    @Mock
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;



    @InjectMocks
    private CompanyServiceImpl companyService;

    private SaveCompanyRequest saveCompanyRequest;
    private SaveAddressRequest saveAddressRequest;
    private User director;
    private MockMultipartFile image;
    private Company company;
    private CompanyDto companyDto;
    private org.example.gobookingcommon.entity.company.Address address;
    private User nonDirector;
    private int companyId;
    private String keyword;
    private PageRequest pageRequest;
    private CompanyResponse companyResponse;

    @BeforeEach
    void setUp() {
        keyword = "Test";
        pageRequest = PageRequest.of(0, 10);

        saveCompanyRequest = new SaveCompanyRequest();
        saveCompanyRequest.setDirectorId(1);

        saveAddressRequest = new SaveAddressRequest();
        saveAddressRequest.setStreet("Main St");
        saveAddressRequest.setApartmentNumber("123");

        director = new User();

        nonDirector = new User();
        nonDirector.setId(2);

        company = new Company();
        company.setId(companyId);
        company.setDirector(director);
        company.setName("Test Company");
        company.setPhone("123456789");
        company.setCompanyPicture("old_picture.png");
        address = new org.example.gobookingcommon.entity.company.Address();
        companyDto = new CompanyDto();
        image = new MockMultipartFile("image", "test.png", "image/png", new byte[10]);

        companyResponse = new CompanyResponse();
        companyResponse.setId(1);
        companyResponse.setName("Test Company");
    }

    @Test
    void shouldThrowExceptionIfDirectorAlreadyHasCompany() {
        when(companyRepository.findCompanyByDirectorId(anyInt())).thenReturn(Optional.of(new Company()));
        assertThrows(CompanyAlreadyExistsException.class, () ->
                companyService.save(saveCompanyRequest, saveAddressRequest, image, director));
    }

    @Test
    void shouldThrowExceptionIfAddressAlreadyExists() {
        when(companyRepository.findCompanyByDirectorId(anyInt())).thenReturn(Optional.empty());
        when(addressService.getAddressByStreetAndApartmentNumber(anyString(), anyString())).thenReturn(true);
        assertThrows(AddressOnlyExistException.class, () ->
                companyService.save(saveCompanyRequest, saveAddressRequest, image, director));
    }

    @Test
    void shouldSaveCompanySuccessfully() {
        when(companyRepository.findCompanyByDirectorId(anyInt())).thenReturn(Optional.empty());
        when(addressService.getAddressByStreetAndApartmentNumber(anyString(), anyString())).thenReturn(false);
        when(companyMapper.toEntity(saveCompanyRequest)).thenReturn(company);
        when(addressMapper.toEntity(saveAddressRequest)).thenReturn(address);
        when(companyRepository.save(any(Company.class))).thenReturn(company);

        companyService.save(saveCompanyRequest, saveAddressRequest, image, director);

        verify(addressService).saveAddress(any());
        verify(companyRepository).save(any());
        verify(userService).editUser(director);
    }

    @Test
    void shouldThrowExceptionOnFileUploadError() throws IOException {
        when(companyRepository.findCompanyByDirectorId(anyInt())).thenReturn(Optional.empty());
        when(addressService.getAddressByStreetAndApartmentNumber(anyString(), anyString())).thenReturn(false);
        when(companyMapper.toEntity(saveCompanyRequest)).thenReturn(company);

        MockMultipartFile spyImage = spy(new MockMultipartFile("image", "test.png", "image/png", new byte[10]));
        doThrow(new IOException("File error")).when(spyImage).transferTo(any(File.class));

        assertThrows(RuntimeException.class, () ->
                companyService.save(saveCompanyRequest, saveAddressRequest, spyImage, director));
    }


    @Test
    void shouldReturnCompanyDtoByDirector() {
        when(companyRepository.findCompanyByDirector(director)).thenReturn(company);
        when(companyMapper.toDto(company)).thenReturn(companyDto);

        CompanyDto result = companyService.getCompanyDtoByDirector(director);

        assertNotNull(result);
        verify(companyRepository).findCompanyByDirector(director);
        verify(companyMapper).toDto(company);
    }

    @Test
    void shouldReturnNullIfCompanyNotFound() {
        when(companyRepository.findCompanyByDirector(director)).thenReturn(null);
        when(companyMapper.toDto(null)).thenReturn(null);

        CompanyDto result = companyService.getCompanyDtoByDirector(director);

        assertNull(result);
        verify(companyRepository).findCompanyByDirector(director);
        verify(companyMapper).toDto(null);
    }

    @Test
    void shouldThrowExceptionIfMappingFails() {
        when(companyRepository.findCompanyByDirector(director)).thenReturn(company);
        when(companyMapper.toDto(company)).thenThrow(new RuntimeException("Mapping error"));

        assertThrows(RuntimeException.class, () -> companyService.getCompanyDtoByDirector(director));

        verify(companyRepository).findCompanyByDirector(director);
        verify(companyMapper).toDto(company);
    }

    @Test
    void shouldThrowExceptionIfCompanyNotFound() {
        int companyId = 1;
        when(companyRepository.getCompanyById(companyId)).thenReturn(null);

        assertThrows(CompanyNotFoundException.class, () -> companyService.deleteCompany(companyId, director));
    }

    @Test
    void shouldThrowExceptionIfDirectorDoesNotMatch() {
        int companyId = 1;
        when(companyRepository.getCompanyById(companyId)).thenReturn(company);


        assertThrows(DirectorNotMatchException.class, () -> companyService.deleteCompany(companyId, nonDirector));
    }

    @Test
    void shouldDeleteCompanySuccessfullyIfDirectorMatches() {
        int companyId = 1;
        when(companyRepository.getCompanyById(companyId)).thenReturn(company);

        companyService.deleteCompany(companyId, director);

        verify(companyRepository).deleteById(companyId);
    }

    @Test
    void shouldHandleEmptyResultDataAccessExceptionWhenDeleting() {
        int companyId = 1;
        when(companyRepository.getCompanyById(companyId)).thenReturn(company);
        doThrow(new EmptyResultDataAccessException(1)).when(companyRepository).deleteById(companyId);

        assertThrows(EmptyResultDataAccessException.class, () -> companyService.deleteCompany(companyId, director));
    }

    @Test
    void shouldReturnCompanyIfFound() {
        when(companyRepository.findById(companyId)).thenReturn(java.util.Optional.of(company));

        Company result = companyService.getCompanyById(companyId);

        assertNotNull(result);
        assertEquals(companyId, result.getId());
        assertEquals("Test Company", result.getName());
        verify(companyRepository).findById(companyId);
    }

    @Test
    void shouldThrowEntityNotFoundExceptionIfCompanyNotFound() {
        when(companyRepository.findById(companyId)).thenReturn(java.util.Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                companyService.getCompanyById(companyId));
        assertEquals("Company not found", exception.getMessage());
        verify(companyRepository).findById(companyId);
    }

    @Test
    void shouldThrowEntityNotFoundExceptionIfCompanyRepositoryThrowsException() {

        when(companyRepository.findById(companyId)).thenThrow(new RuntimeException("Database error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                companyService.getCompanyById(companyId));
        assertEquals("Database error", exception.getMessage());
        verify(companyRepository).findById(companyId);
    }

    @Test
    void shouldReturnCompaniesByKeyword() {
        Page<Company> companyPage = new PageImpl<>(java.util.List.of(company));
        when(companyRepository.findCompaniesByNameContainingAndValid(keyword, pageRequest, true)).thenReturn(companyPage);
        when(companyMapper.toResponse(company)).thenReturn(companyResponse);

        Page<CompanyResponse> result = companyService.companyByKeyword(keyword, pageRequest);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(companyResponse, result.getContent().get(0));
        verify(companyRepository).findCompaniesByNameContainingAndValid(keyword, pageRequest, true);
        verify(companyMapper).toResponse(company);
    }

    @Test
    void shouldReturnEmptyPageIfNoCompaniesFound() {
        Page<Company> companyPage = new PageImpl<>(java.util.List.of());
        when(companyRepository.findCompaniesByNameContainingAndValid(keyword, pageRequest, true)).thenReturn(companyPage);

        Page<CompanyResponse> result = companyService.companyByKeyword(keyword, pageRequest);

        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        assertTrue(result.getContent().isEmpty());
        verify(companyRepository).findCompaniesByNameContainingAndValid(keyword, pageRequest, true);
        verify(companyMapper, never()).toResponse(any());
    }

    @Test
    void shouldThrowExceptionWhenCompanyMapperFails() {
        Page<Company> companyPage = new PageImpl<>(java.util.List.of(company));
        when(companyRepository.findCompaniesByNameContainingAndValid(keyword, pageRequest, true)).thenReturn(companyPage);
        when(companyMapper.toResponse(company)).thenThrow(new RuntimeException("Mapping error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                companyService.companyByKeyword(keyword, pageRequest));
        assertEquals("Mapping error", exception.getMessage());
        verify(companyRepository).findCompaniesByNameContainingAndValid(keyword, pageRequest, true);
        verify(companyMapper).toResponse(company);
    }

    @Test
    void shouldReturnEmptyPageWhenKeywordIsEmpty() {
        keyword = "";
        Page<Company> companyPage = new PageImpl<>(java.util.List.of());
        when(companyRepository.findCompaniesByNameContainingAndValid(keyword, pageRequest, true)).thenReturn(companyPage);

        Page<CompanyResponse> result = companyService.companyByKeyword(keyword, pageRequest);

        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        assertTrue(result.getContent().isEmpty());
        verify(companyRepository).findCompaniesByNameContainingAndValid(keyword, pageRequest, true);
        verify(companyMapper, never()).toResponse(any());
    }

    @Test
    void shouldReturnCompaniesForValidPageRequest() {
        Page<Company> companyPage = new PageImpl<>(java.util.List.of(company));
        when(companyRepository.findCompaniesByNameContainingAndValid(keyword, pageRequest, true)).thenReturn(companyPage);
        when(companyMapper.toResponse(company)).thenReturn(companyResponse);

        Page<CompanyResponse> result = companyService.companyByKeyword(keyword, pageRequest);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(companyResponse, result.getContent().get(0));
        assertEquals(1, result.getNumberOfElements());
        assertEquals(0, result.getNumber());
        verify(companyRepository).findCompaniesByNameContainingAndValid(keyword, pageRequest, true);
        verify(companyMapper).toResponse(company);
    }

    @Test
    void shouldReturnCompaniesSuccessfully() {
        Page<Company> companyPage = new PageImpl<>(java.util.List.of(company));
        when(companyRepository.findCompaniesByValid(true, pageRequest)).thenReturn(companyPage);
        when(companyMapper.toResponse(company)).thenReturn(companyResponse);

        Page<CompanyResponse> result = companyService.getAllCompanies(pageRequest);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(companyResponse, result.getContent().get(0));
        verify(companyRepository).findCompaniesByValid(true, pageRequest);
        verify(companyMapper).toResponse(company);
    }
    @Test
    void shouldReturnEmptyPageWhenNoValidCompanies() {
        Page<Company> companyPage = new PageImpl<>(java.util.List.of());
        when(companyRepository.findCompaniesByValid(true, pageRequest)).thenReturn(companyPage);

        Page<CompanyResponse> result = companyService.getAllCompanies(pageRequest);

        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        assertTrue(result.getContent().isEmpty());
        verify(companyRepository).findCompaniesByValid(true, pageRequest);
        verify(companyMapper, never()).toResponse(any());
    }

    @Test
    void shouldHandleEmptyPageRequest() {
        PageRequest emptyPageRequest = PageRequest.of(0, 1);
        Page<Company> companyPage = new PageImpl<>(java.util.List.of());
        when(companyRepository.findCompaniesByValid(true, emptyPageRequest)).thenReturn(companyPage);

        Page<CompanyResponse> result = companyService.getAllCompanies(emptyPageRequest);

        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        assertTrue(result.getContent().isEmpty());
        verify(companyRepository).findCompaniesByValid(true, emptyPageRequest);
        verify(companyMapper, never()).toResponse(any());
    }

    @Test
    void shouldReturnMultiplePagesOfCompanies() {
        Company company1 = new Company();
        company1.setId(1);
        company1.setName("Company 1");

        Company company2 = new Company();
        company2.setId(2);
        company2.setName("Company 2");

        Company company3 = new Company();
        company3.setId(3);
        company3.setName("Company 3");

        CompanyResponse companyResponse1 = new CompanyResponse();
        companyResponse1.setName("Company 1");

        CompanyResponse companyResponse2 = new CompanyResponse();
        companyResponse2.setName("Company 2");

        CompanyResponse companyResponse3 = new CompanyResponse();
        companyResponse3.setName("Company 3");

        PageRequest pageRequest = PageRequest.of(0, 3);  // Get the first page with 3 items per page
        Page<Company> companyPage = new PageImpl<>(java.util.List.of(company1, company2, company3), pageRequest, 3);

        when(companyRepository.findCompaniesByValid(true, pageRequest)).thenReturn(companyPage);

        when(companyMapper.toResponse(company1)).thenReturn(companyResponse1);
        when(companyMapper.toResponse(company2)).thenReturn(companyResponse2);
        when(companyMapper.toResponse(company3)).thenReturn(companyResponse3);

        Page<CompanyResponse> result = companyService.getAllCompanies(pageRequest);

        assertNotNull(result);
        assertEquals(3, result.getTotalElements());
        assertEquals(3, result.getContent().size());
        assertEquals("Company 1", result.getContent().get(0).getName());
        assertEquals("Company 2", result.getContent().get(1).getName());
        assertEquals("Company 3", result.getContent().get(2).getName());

        verify(companyRepository).findCompaniesByValid(true, pageRequest);
        verify(companyMapper, times(3)).toResponse(any(Company.class));  // Verify the mapper was called for each company
    }

    @Test
    void shouldReturnCorrectCountForValidCompanies() {
        when(companyRepository.countCompaniesByValid(true)).thenReturn(5);

        int result = companyService.countCompaniesByValid(true);

        assertEquals(5, result);
        verify(companyRepository).countCompaniesByValid(true);
    }

    @Test
    void shouldReturnCorrectCountForInvalidCompanies() {
        when(companyRepository.countCompaniesByValid(false)).thenReturn(3);

        int result = companyService.countCompaniesByValid(false);

        assertEquals(3, result);
        verify(companyRepository).countCompaniesByValid(false);
    }

    @Test
    void shouldReturnZeroWhenNoValidCompanies() {
        when(companyRepository.countCompaniesByValid(true)).thenReturn(0);

        int result = companyService.countCompaniesByValid(true);

        assertEquals(0, result);
        verify(companyRepository).countCompaniesByValid(true);
    }

    @Test
    void shouldReturnZeroWhenNoInvalidCompanies() {
        when(companyRepository.countCompaniesByValid(false)).thenReturn(0);

        int result = companyService.countCompaniesByValid(false);

        assertEquals(0, result);
        verify(companyRepository).countCompaniesByValid(false);
    }

    @Test
    void shouldHandleRepositoryReturningZero() {
        when(companyRepository.countCompaniesByValid(true)).thenReturn(0);
        when(companyRepository.countCompaniesByValid(false)).thenReturn(0);

        int validCount = companyService.countCompaniesByValid(true);
        int invalidCount = companyService.countCompaniesByValid(false);

        assertEquals(0, validCount);
        assertEquals(0, invalidCount);
        verify(companyRepository, times(1)).countCompaniesByValid(true);
        verify(companyRepository, times(1)).countCompaniesByValid(false);
    }

    @Test
    void shouldReturnValidCompaniesPage() {
        // Given: Mock valid companies
        Company company1 = new Company();
        company1.setId(1);
        company1.setName("Valid Company 1");

        Company company2 = new Company();
        company2.setId(2);
        company2.setName("Valid Company 2");

        CompanyForAdminDto companyDto1 = new CompanyForAdminDto();
        companyDto1.setName("Valid Company 1");

        CompanyForAdminDto companyDto2 = new CompanyForAdminDto();
        companyDto2.setName("Valid Company 2");

        PageRequest pageRequest = PageRequest.of(0, 2);
        Page<Company> companyPage = new PageImpl<>(List.of(company1, company2), pageRequest, 2);

        when(companyRepository.findCompaniesByValid(true, pageRequest)).thenReturn(companyPage);
        when(companyMapper.toAdminDto(company1)).thenReturn(companyDto1);
        when(companyMapper.toAdminDto(company2)).thenReturn(companyDto2);

        Page<CompanyForAdminDto> result = companyService.getAllCompaniesByValid(true, pageRequest);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());
        assertEquals("Valid Company 1", result.getContent().get(0).getName());
        assertEquals("Valid Company 2", result.getContent().get(1).getName());
        verify(companyRepository).findCompaniesByValid(true, pageRequest);
        verify(companyMapper, times(2)).toAdminDto(any(Company.class));
    }

    @Test
    void shouldReturnInvalidCompaniesPage() {
        // Given: Mock invalid companies
        Company company1 = new Company();
        company1.setId(1);
        company1.setName("Invalid Company 1");

        Company company2 = new Company();
        company2.setId(2);
        company2.setName("Invalid Company 2");

        CompanyForAdminDto companyDto1 = new CompanyForAdminDto();
        companyDto1.setName("Invalid Company 1");

        CompanyForAdminDto companyDto2 = new CompanyForAdminDto();
        companyDto2.setName("Invalid Company 2");

        PageRequest pageRequest = PageRequest.of(0, 2);
        Page<Company> companyPage = new PageImpl<>(List.of(company1, company2), pageRequest, 2);

        when(companyRepository.findCompaniesByValid(false, pageRequest)).thenReturn(companyPage);
        when(companyMapper.toAdminDto(company1)).thenReturn(companyDto1);
        when(companyMapper.toAdminDto(company2)).thenReturn(companyDto2);

        Page<CompanyForAdminDto> result = companyService.getAllCompaniesByValid(false, pageRequest);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());
        assertEquals("Invalid Company 1", result.getContent().get(0).getName());
        assertEquals("Invalid Company 2", result.getContent().get(1).getName());
        verify(companyRepository).findCompaniesByValid(false, pageRequest);
        verify(companyMapper, times(2)).toAdminDto(any(Company.class));
    }

    @Test
    void shouldReturnEmptyPageWhenNoCompaniesFound() {
        PageRequest pageRequest = PageRequest.of(0, 2);
        Page<Company> companyPage = new PageImpl<>(List.of(), pageRequest, 0);

        when(companyRepository.findCompaniesByValid(true, pageRequest)).thenReturn(companyPage);

        Page<CompanyForAdminDto> result = companyService.getAllCompaniesByValid(true, pageRequest);

        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        assertEquals(0, result.getContent().size());
        verify(companyRepository).findCompaniesByValid(true, pageRequest);
    }

    @Test
    void shouldReturnCorrectPageSizeAndTotal() {
        Company company1 = new Company();
        company1.setId(1);
        company1.setName("Company 1");

        Company company2 = new Company();
        company2.setId(2);
        company2.setName("Company 2");

        CompanyForAdminDto companyDto1 = new CompanyForAdminDto();
        companyDto1.setName("Company 1");

        CompanyForAdminDto companyDto2 = new CompanyForAdminDto();
        companyDto2.setName("Company 2");

        PageRequest pageRequest = PageRequest.of(0, 2);
        Page<Company> companyPage = new PageImpl<>(List.of(company1, company2), pageRequest, 2);

        when(companyRepository.findCompaniesByValid(true, pageRequest)).thenReturn(companyPage);
        when(companyMapper.toAdminDto(company1)).thenReturn(companyDto1);
        when(companyMapper.toAdminDto(company2)).thenReturn(companyDto2);

        Page<CompanyForAdminDto> result = companyService.getAllCompaniesByValid(true, pageRequest);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());
        verify(companyRepository).findCompaniesByValid(true, pageRequest);
    }

    @Test
    void shouldMapCompaniesToDtoCorrectly() {
        Company company1 = new Company();
        company1.setId(1);
        company1.setName("Company 1");

        CompanyForAdminDto companyDto1 = new CompanyForAdminDto();
        companyDto1.setName("Company 1");

        PageRequest pageRequest = PageRequest.of(0, 1);
        Page<Company> companyPage = new PageImpl<>(List.of(company1), pageRequest, 1);

        when(companyRepository.findCompaniesByValid(true, pageRequest)).thenReturn(companyPage);
        when(companyMapper.toAdminDto(company1)).thenReturn(companyDto1);

        Page<CompanyForAdminDto> result = companyService.getAllCompaniesByValid(true, pageRequest);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Company 1", result.getContent().get(0).getName());
        verify(companyMapper).toAdminDto(company1);
    }

    @Test
    void shouldThrowCompanyNotFoundExceptionIfCompanyDoesNotExist() {
        // Given: Company doesn't exist
        when(companyRepository.getCompanyById(anyInt())).thenReturn(null);

        // When & Then: Verify that exception is thrown
        assertThrows(CompanyNotFoundException.class, () ->
                companyService.editCompany(saveCompanyRequest, 1, image, saveAddressRequest, 1, director));
    }

    @Test
    void shouldThrowDirectorNotMatchExceptionIfDirectorDoesNotMatch() {
        User anotherUser = new User();
        anotherUser.setId(2);

        lenient().when(companyRepository.getCompanyById(anyInt())).thenReturn(company);
        lenient().when(userRepository.getUserById(anyInt())).thenReturn(anotherUser);

        DirectorNotMatchException exception = assertThrows(DirectorNotMatchException.class, () ->
                companyService.editCompany(saveCompanyRequest, 1, image, saveAddressRequest, 1, anotherUser));

        assertEquals("Director does not match", exception.getMessage());
    }
    @Test
    void shouldThrowRuntimeExceptionForUserUpdateError() {
        when(companyRepository.getCompanyById(anyInt())).thenReturn(company);
        when(userRepository.getUserById(anyInt())).thenThrow(new RuntimeException("User update error"));

        assertThrows(RuntimeException.class, () ->
                companyService.editCompany(saveCompanyRequest, 1, image, saveAddressRequest, 1, director));
    }

    @Test
    void shouldReturnFalseForValidPngImage() {
        MultipartFile image = mock(MultipartFile.class);
        when(image.getContentType()).thenReturn("image/png");

        boolean result = companyService.isValidImage(image);

        assertFalse(result, "PNG image should be valid");
    }

    @Test
    void shouldReturnFalseForValidJpegImage() {
        MultipartFile image = mock(MultipartFile.class);
        when(image.getContentType()).thenReturn("image/jpeg");

        boolean result = companyService.isValidImage(image);

        assertFalse(result, "JPEG image should be valid");
    }

    @Test
    void shouldReturnTrueForInvalidImageFormat() {
        MultipartFile image = mock(MultipartFile.class);
        when(image.getContentType()).thenReturn("image/gif");

        boolean result = companyService.isValidImage(image);

        assertTrue(result, "GIF image should be invalid");
    }

    @Test
    void shouldReturnTrueForNullContentType() {
        MultipartFile image = mock(MultipartFile.class);
        when(image.getContentType()).thenReturn(null);

        boolean result = companyService.isValidImage(image);

        assertTrue(result, "Null content type should be invalid");
    }

    @Test
    void shouldReturnTrueForEmptyContentType() {
        MultipartFile image = mock(MultipartFile.class);
        when(image.getContentType()).thenReturn("");

        boolean result = companyService.isValidImage(image);

        assertTrue(result, "Empty content type should be invalid");
    }

    @Test
    void shouldReturnTrueForInvalidContentType() {
        MultipartFile image = mock(MultipartFile.class);
        when(image.getContentType()).thenReturn("text/plain");

        boolean result = companyService.isValidImage(image);

        assertTrue(result, "Text file should be invalid");
    }

    @Test
    void shouldReturnCompanyResponseWhenCompanyFound() {

        when(companyRepository.findCompanyByDirectorId(anyInt())).thenReturn(Optional.of(company));


        when(companyMapper.toResponse(company)).thenReturn(companyResponse);


        CompanyResponse result = companyService.getCompanyResponseByDirectorId(1);

        assertNotNull(result, "The company response should not be null");
        assertEquals(companyResponse.getId(), result.getId(), "The company id should match");
        assertEquals(companyResponse.getName(), result.getName(), "The company name should match");

        verify(companyRepository).findCompanyByDirectorId(1);
        verify(companyMapper).toResponse(company);
    }

    @Test
    void shouldThrowEntityNotFoundExceptionWhenCompanyNotFound() {
        when(companyRepository.findCompanyByDirectorId(anyInt())).thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () ->
                companyService.getCompanyResponseByDirectorId(1)
        );

        assertEquals("Company not found", thrown.getMessage(), "The exception message should match");

        verify(companyRepository).findCompanyByDirectorId(1);
        verifyNoInteractions(companyMapper);
    }
}


