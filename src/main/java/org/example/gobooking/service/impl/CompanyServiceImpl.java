package org.example.gobooking.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.gobooking.customException.AddressOnlyExistException;
import org.example.gobooking.customException.CompanyAlreadyExistsException;
import org.example.gobooking.dto.company.CompanyDto;
import org.example.gobooking.dto.company.CompanyResponse;
import org.example.gobooking.dto.company.SaveAddressRequest;
import org.example.gobooking.dto.company.SaveCompanyRequest;
import org.example.gobooking.entity.company.Address;
import org.example.gobooking.entity.company.Company;
import org.example.gobooking.entity.user.User;
import org.example.gobooking.mapper.AddressMapper;
import org.example.gobooking.mapper.CompanyMapper;
import org.example.gobooking.repository.CompanyRepository;
import org.example.gobooking.service.AddressService;
import org.example.gobooking.service.CompanyService;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;
    private final AddressService addressService;
    private final AddressMapper addressMapper;

    @Value("${image.upload.path}")
    private String imageUploadPath;

    @Override
    public void save(SaveCompanyRequest saveCompanyRequest, SaveAddressRequest saveAddressRequest, MultipartFile image) {
        if (companyRepository.findCompanyByDirectorId(saveCompanyRequest.getDirectorId()).isPresent()) {
            throw new CompanyAlreadyExistsException("Director already has an associated company.");
        }
        if (addressService.getAddressByStreetAndApartmentNumber(saveAddressRequest.getStreet(), saveAddressRequest.getApartmentNumber())) {
            throw new AddressOnlyExistException("Address only exist");
        }
        Company company = companyMapper.toEntity(saveCompanyRequest);
        try {
            if (image != null && !image.isEmpty()) {
                if (!isValidImage(image)) {
                    throw new IllegalArgumentException("Invalid image format");
                }
                String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
                File file = new File(imageUploadPath, fileName);
                image.transferTo(file);
                company.setCompanyPicture(fileName);
            }
        }catch (IOException e) {
            throw new RuntimeException("File upload error: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("User update error: " + e.getMessage(), e);
        }

        Address address = addressMapper.toEntity(saveAddressRequest);
        addressService.saveAddress(address);
        company.setAddress(address);
        companyRepository.save(company);
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
    public void deleteCompany(int id) {
        companyRepository.deleteById(id);
    }

    @Override
    @Named("getCompanyById")
    public Company getCompanyById(int id) {
        return companyRepository.findById(id).orElse(null);
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
    public CompanyResponse getCompanyResponseById(int id) {
        Optional<Company> byId = companyRepository.findById(id);
        if (byId.isPresent()) {
            return companyMapper.toResponse(byId.get());
        }
        throw new EntityNotFoundException("Company not found");
    }

    private boolean isValidImage(MultipartFile image) {
        String contentType = image.getContentType();
        return contentType != null && (contentType.equals("image/png") || contentType.equals("image/jpeg"));
    }

}
