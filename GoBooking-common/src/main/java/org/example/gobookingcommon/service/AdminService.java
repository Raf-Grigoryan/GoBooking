package org.example.gobookingcommon.service;


import org.example.gobookingcommon.dto.admin.AdminAnalyticDto;

public interface AdminService {

    void deleteCompany(int directorId);

    AdminAnalyticDto getadminAnalyticDto();
}
