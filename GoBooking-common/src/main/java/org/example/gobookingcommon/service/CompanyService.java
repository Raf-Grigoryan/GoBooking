package org.example.gobookingcommon.service;


import org.example.gobookingcommon.dto.company.*;
import org.example.gobookingcommon.entity.company.Company;
import org.example.gobookingcommon.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

public interface CompanyService {

    void save(SaveCompanyRequest saveCompanyRequest, SaveAddressRequest saveAddressRequest, MultipartFile image, User director);

    CompanyDto getCompanyDtoByDirector(User director);

    void deleteCompany(int id, User user);

    Company getCompanyById(int id);

    Company getCompanyByDirector(User director);

    Page<CompanyResponse> companyByKeyword(String keyword, PageRequest pageRequest);

    Page<CompanyResponse> getAllCompanies(PageRequest pageRequest);

    int countCompaniesByValid(boolean valid);

    Page<CompanyForAdminDto> getAllCompaniesByValid(boolean valid, PageRequest pageRequest);

    void editCompany(SaveCompanyRequest companyRequest, int id, MultipartFile image, SaveAddressRequest addressRequest, int addressId, User user);

    CompanyResponse getCompanyResponseByDirectorId(int doctorId);
}
