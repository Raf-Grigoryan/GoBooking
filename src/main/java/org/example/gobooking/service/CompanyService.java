package org.example.gobooking.service;

import org.example.gobooking.dto.company.CompanyDto;
import org.example.gobooking.dto.company.SaveAddressRequest;
import org.example.gobooking.dto.company.SaveCompanyRequest;
import org.example.gobooking.entity.user.User;


public interface CompanyService {

    void save (SaveCompanyRequest saveCompanyRequest, SaveAddressRequest saveAddressRequest);

    CompanyDto getCompanyByDirector(User director);

    void deleteCompany(int id);
}
