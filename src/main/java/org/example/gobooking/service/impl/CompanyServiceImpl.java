package org.example.gobooking.service.impl;

import jakarta.persistence.EntityNotFoundException;
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
import org.example.gobooking.service.UserService;
import org.mapstruct.Named;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;
    private final AddressService addressService;
    private final AddressMapper addressMapper;
    private final UserService userService;


    @Override
    public void save(SaveCompanyRequest saveCompanyRequest, SaveAddressRequest saveAddressRequest, User director) {
        if (companyRepository.findCompanyByDirectorId(saveCompanyRequest.getDirectorId()).isPresent()) {
            throw new CompanyAlreadyExistsException("Director already has an associated company.");
        }
        if (addressService.getAddressByStreetAndApartmentNumber(saveAddressRequest.getStreet(), saveAddressRequest.getApartmentNumber())) {
            throw new AddressOnlyExistException("Address only exist");
        }
        Address address = addressMapper.toEntity(saveAddressRequest);
        addressService.saveAddress(address);
        Company company = companyMapper.toEntity(saveCompanyRequest);
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

}
