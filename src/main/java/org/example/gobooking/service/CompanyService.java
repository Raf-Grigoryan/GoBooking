package org.example.gobooking.service;

import org.example.gobooking.dto.company.CompanyDto;
import org.example.gobooking.dto.company.CompanyResponse;
import org.example.gobooking.dto.company.SaveAddressRequest;
import org.example.gobooking.dto.company.SaveCompanyRequest;
import org.example.gobooking.entity.company.Company;
import org.example.gobooking.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;


public interface CompanyService {

    void save(SaveCompanyRequest saveCompanyRequest, SaveAddressRequest saveAddressRequest, User director);

    void save(SaveCompanyRequest saveCompanyRequest, SaveAddressRequest saveAddressRequest, MultipartFile image);

    CompanyDto getCompanyDtoByDirector(User director);

    void deleteCompany(int id);

    Company getCompanyById(int id);

    Company getCompanyByDirector(User director);

    Page<CompanyResponse> companyByKeyword(String keyword, PageRequest pageRequest);

    Page<CompanyResponse> getAllCompanies(PageRequest pageRequest);

}
