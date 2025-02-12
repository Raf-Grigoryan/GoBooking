package org.example.gobooking.service;

import org.example.gobooking.dto.company.CompanyDto;
import org.example.gobooking.dto.company.SaveAddressRequest;
import org.example.gobooking.dto.company.SaveCompanyRequest;
import org.example.gobooking.entity.company.Company;
import org.example.gobooking.entity.user.User;


public interface CompanyService {

    void save (SaveCompanyRequest saveCompanyRequest, SaveAddressRequest saveAddressRequest);

    CompanyDto getCompanyDtoByDirector(User director);

    void deleteCompany(int id);

    Company getCompanyById(int id);

    Company getCompanyByDirector(User director);
}
