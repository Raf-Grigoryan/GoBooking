package org.example.gobookingcommon.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.gobookingcommon.customException.CompanyNotFoundException;
import org.example.gobookingcommon.customException.InvalidRoleException;
import org.example.gobookingcommon.customException.UserNotFoundException;
import org.example.gobookingcommon.dto.admin.AdminAnalyticDto;
import org.example.gobookingcommon.entity.company.Company;
import org.example.gobookingcommon.entity.user.Role;
import org.example.gobookingcommon.entity.user.User;
import org.example.gobookingcommon.repository.CompanyRepository;
import org.example.gobookingcommon.repository.UserRepository;
import org.example.gobookingcommon.service.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final CompanyService companyService;
    private final ProjectFinanceService projectFinanceService;
    private final BookingBalanceService bookingBalanceService;

    @Transactional
    @Override
    public void deleteCompany(int directorId) {
        User user = userRepository.findById(directorId).orElseThrow(() -> new UserNotFoundException("Director not found"));
        if (user.getRole() == Role.DIRECTOR) {
            throw new InvalidRoleException("User Role is not exist");
        }
        if (user.getCompany() == null) {
            throw new CompanyNotFoundException("Company not found");
        }

        Company company = user.getCompany();
        if (companyRepository.existsCompanyByDirector(user)) {
            List<User> workers = userRepository.findUserByCompany_Id(company.getId());
            user.setCompany(null);
            user.setRole(Role.USER);
            for (User worker : workers) {
                worker.setCompany(null);
                worker.setRole(Role.USER);
                userRepository.save(worker);
            }
            userRepository.save(user);
            companyRepository.delete(company);

        }
    }

    @Override
    public AdminAnalyticDto getadminAnalyticDto() {
        List<String> labels = List.of(
                "Jan", "Feb", "Mar", "Apr", "May", "Jun",
                "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
        );
        return AdminAnalyticDto.builder()
                .adminAnalytic(userService.analyticUsers())
                .labels(labels)
                .companyValid(companyService.countCompaniesByValid(true))
                .companyNotValid(companyService.countCompaniesByValid(false))
                .projectFinance(projectFinanceService.getProjectFinance())
                .bookingBalance(bookingBalanceService.getBookingBalance())
                .AllRolesUsersCount(userService.getAllRolesUsersCount())
                .build();
    }
}
