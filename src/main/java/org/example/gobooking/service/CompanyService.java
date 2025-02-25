package org.example.gobooking.service;

import org.example.gobooking.dto.company.*;
import org.example.gobooking.entity.company.Company;
import org.example.gobooking.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;


public interface CompanyService {

    void save(SaveCompanyRequest saveCompanyRequest, SaveAddressRequest saveAddressRequest, MultipartFile image, User director);

    CompanyDto getCompanyDtoByDirector(User director);

    void deleteCompany(int id);

    Company getCompanyById(int id);

    Company getCompanyByDirector(User director);

    Page<CompanyResponse> companyByKeyword(String keyword, PageRequest pageRequest);

    Page<CompanyResponse> getAllCompanies(PageRequest pageRequest);

    int countCompaniesByValid(boolean valid);

    Page<CompanyForAdminDto> getAllCompaniesByValid(boolean valid, PageRequest pageRequest);

}
