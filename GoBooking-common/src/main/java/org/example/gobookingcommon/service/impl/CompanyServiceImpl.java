package org.example.gobookingcommon.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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
import org.example.gobookingcommon.service.AddressService;
import org.example.gobookingcommon.service.CompanyService;
import org.example.gobookingcommon.service.UserService;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;


@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;
    private final AddressService addressService;
    private final AddressMapper addressMapper;
    private final UserService userService;
    private final UserRepository userRepository;

    @Value("${image.upload.path}")
    private String imageUploadPath;

    @Override
    public void save(SaveCompanyRequest saveCompanyRequest, SaveAddressRequest saveAddressRequest, MultipartFile image, User director) {
        if (companyRepository.findCompanyByDirectorId(saveCompanyRequest.getDirectorId()).isPresent()) {
            throw new CompanyAlreadyExistsException("Director already has an associated company.");
        }
        if (addressService.getAddressByStreetAndApartmentNumber(saveAddressRequest.getStreet(), saveAddressRequest.getApartmentNumber())) {
            throw new AddressOnlyExistException("Address only exist");
        }
        Company company = companyMapper.toEntity(saveCompanyRequest);
        try {
            if (image != null && !image.isEmpty()) {
                if (isValidImage(image)) {
                    throw new IllegalArgumentException("Invalid image format");
                }
                String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
                File file = new File(imageUploadPath, fileName);
                image.transferTo(file);
                company.setCompanyPicture(fileName);
            }
        } catch (IOException e) {
            throw new RuntimeException("File upload error: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("User update error: " + e.getMessage(), e);
        }

        org.example.gobookingcommon.entity.company.Address address = addressMapper.toEntity(saveAddressRequest);
        addressService.saveAddress(address);
        company.setAddress(address);
        director.setCompany(companyRepository.save(company));
        userService.editUser(director);
    }


    @Override
    public CompanyDto getCompanyDtoByDirector(User director) {
        return companyMapper.toDto(companyRepository.findCompanyByDirector(director));
    }

    @Override
    public Company getCompanyByDirector(User director) {
        return companyRepository.findCompanyByDirector(director);
    }

    @Transactional
    @Override
    public void deleteCompany(int id, User user) {
       Company company = companyRepository.getCompanyById(id);
        if (company == null){
            throw new CompanyNotFoundException("Company not exist");
        }
        if (!company.getDirector().equals(user)){
            throw new DirectorNotMatchException("Director does not match");
        }
        companyRepository.deleteById(id);
    }

    @Override
    @Named("getCompanyById")
    public Company getCompanyById(int id) {
        return companyRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Company not found"));
    }

    @Override
    public Page<CompanyResponse> companyByKeyword(String keyword, PageRequest pageRequest) {
        Page<Company> companies = companyRepository.findCompaniesByNameContainingAndValid(keyword, pageRequest, true);
        return companies.map(companyMapper::toResponse);
    }

    @Override
    public Page<CompanyResponse> getAllCompanies(PageRequest pageRequest) {
        Page<Company> companies = companyRepository.findCompaniesByValid(true, pageRequest);
        return companies.map(companyMapper::toResponse);
    }

    @Override
    public int countCompaniesByValid(boolean valid) {
        return companyRepository.countCompaniesByValid(valid);
    }

    @Override
    public Page<CompanyForAdminDto> getAllCompaniesByValid(boolean valid, PageRequest pageRequest) {
        Page<Company> companies = companyRepository.findCompaniesByValid(valid, pageRequest);
        return companies.map(companyMapper::toAdminDto);
    }


    @Override
    public void editCompany(SaveCompanyRequest companyRequest, int id, MultipartFile image, SaveAddressRequest addressRequest, int addressId, User user) {
        Company company = companyRepository.getCompanyById(id);
        if (company == null){
            throw new CompanyNotFoundException("Company not exist");
        }
        if (!company.getDirector().equals(user)){
            throw new DirectorNotMatchException("Director does not match");
        }
        try {
            company.setId(id);
            company.setName(companyRequest.getName());
            company.setPhone(companyRequest.getPhone());
            if (image != null && !image.isEmpty()) {
                if (isValidImage(image)) {
                    throw new IllegalArgumentException("Invalid image format");
                }
                String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
                File file = new File(imageUploadPath, fileName);
                image.transferTo(file);
                company.setCompanyPicture(fileName);
            }
            company.setDirector(userRepository.getUserById(companyRequest.getDirectorId()));
            company.setAddress(addressService.editAddress(addressRequest, addressId));
        } catch (IOException e) {
            throw new RuntimeException("File upload error: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("User update error: " + e.getMessage(), e);
        }
        companyRepository.save(company);
    }

     boolean isValidImage(MultipartFile image) {
        String contentType = image.getContentType();
        return contentType == null || (!contentType.equals("image/png") && !contentType.equals("image/jpeg"));
    }

    @Override
    public CompanyResponse getCompanyResponseByDirectorId(int doctorId) {
        Company company = companyRepository.findCompanyByDirectorId(doctorId)
                .orElseThrow(() -> new EntityNotFoundException("Company not found"));
        return companyMapper.toResponse(company);
    }
}
