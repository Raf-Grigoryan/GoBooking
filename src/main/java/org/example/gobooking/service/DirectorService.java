package org.example.gobooking.service;

import org.example.gobooking.dto.company.CompanyManagement;
import org.example.gobooking.dto.request.SaveRoleChangeRequest;

public interface DirectorService {

    void sendWorkRequest(SaveRoleChangeRequest request);

    CompanyManagement getCompanyManagementByDirectorId(int directorId);

}
