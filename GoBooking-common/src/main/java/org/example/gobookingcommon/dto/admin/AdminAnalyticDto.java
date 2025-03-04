package org.example.gobookingcommon.dto.admin;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AdminAnalyticDto {

    List<Integer> adminAnalytic;
    List<String> labels;
    int companyValid;
    int companyNotValid;
    double projectFinance;
    double bookingBalance;
    List<Integer> AllRolesUsersCount;
}
