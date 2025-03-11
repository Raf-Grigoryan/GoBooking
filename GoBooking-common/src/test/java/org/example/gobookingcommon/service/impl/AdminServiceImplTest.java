package org.example.gobookingcommon.service.impl;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.example.gobookingcommon.dto.admin.AdminAnalyticDto;
import org.example.gobookingcommon.entity.company.Company;
import org.example.gobookingcommon.entity.user.Role;
import org.example.gobookingcommon.entity.user.User;
import org.example.gobookingcommon.repository.CompanyRepository;
import org.example.gobookingcommon.repository.UserRepository;
import org.example.gobookingcommon.service.BookingBalanceService;
import org.example.gobookingcommon.service.CompanyService;
import org.example.gobookingcommon.service.ProjectFinanceService;
import org.example.gobookingcommon.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AdminServiceImplTest {

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @Mock
    private CompanyService companyService;

    @Mock
    private ProjectFinanceService projectFinanceService;

    @Mock
    private BookingBalanceService bookingBalanceService;

    @InjectMocks
    private AdminServiceImpl adminService;

    private User director;
    private Company company;

    @BeforeEach
    void setUp() {
        director = new User();
        director.setId(1);
        director.setRole(Role.DIRECTOR);
        company = new Company();
        company.setId(1);
        director.setCompany(company);

    }

    @Test
    void deleteCompany_validDirector_success() {
        when(userRepository.findById(director.getId())).thenReturn(Optional.of(director));
        when(companyRepository.existsCompanyByDirector(director)).thenReturn(true);
        when(userRepository.findUserByCompany_Id(company.getId())).thenReturn(new ArrayList<>());
        adminService.deleteCompany(director.getId());
        verify(userRepository).save(director);
        verify(companyRepository).delete(company);
    }

    @Test
    void getAdminAnalyticDto_returnsCorrectData() {
        when(userService.analyticUsers()).thenReturn(List.of(1, 2, 3));  // Return a List<Integer> for adminAnalytic
        when(companyService.countCompaniesByValid(true)).thenReturn(5);
        when(companyService.countCompaniesByValid(false)).thenReturn(2);
        when(projectFinanceService.getProjectFinance()).thenReturn(100.50);  // Return double for projectFinance
        when(bookingBalanceService.getBookingBalance()).thenReturn(200.75);  // Return double for bookingBalance
        when(userService.getAllRolesUsersCount()).thenReturn(List.of(10, 20, 30));  // Return List<Integer> for AllRolesUsersCount

        AdminAnalyticDto result = adminService.getadminAnalyticDto();

        assertNotNull(result);
        assertEquals(List.of(1, 2, 3), result.getAdminAnalytic());  // Verify the adminAnalytic list
        assertEquals(5, result.getCompanyValid());
        assertEquals(2, result.getCompanyNotValid());
        assertEquals(100.50, result.getProjectFinance());
        assertEquals(200.75, result.getBookingBalance());
        assertEquals(List.of(10, 20, 30), result.getAllRolesUsersCount());  // Verify the AllRolesUsersCount list
        assertEquals(List.of("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"), result.getLabels());
    }

}
