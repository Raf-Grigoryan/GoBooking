package org.example.gobookingcommon.service;


import org.example.gobookingcommon.dto.company.CompanyManagement;
import org.example.gobookingcommon.dto.request.SaveRoleChangeRequest;
import org.example.gobookingcommon.entity.user.User;

public interface DirectorService {

    void sendWorkRequest(SaveRoleChangeRequest request, User user);

    CompanyManagement getCompanyManagementByDirectorId(int directorId);

}
