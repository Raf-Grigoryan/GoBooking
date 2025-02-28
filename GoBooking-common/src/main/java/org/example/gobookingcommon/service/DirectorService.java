package org.example.gobookingcommon.service;


import org.example.gobookingcommon.dto.company.CompanyManagement;
import org.example.gobookingcommon.dto.request.SaveRoleChangeRequest;

public interface DirectorService {

    void sendWorkRequest(SaveRoleChangeRequest request);

    CompanyManagement getCompanyManagementByDirectorId(int directorId);

}
