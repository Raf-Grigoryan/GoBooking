package org.example.gobooking.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.gobooking.customException.CompanyNoCreateException;
import org.example.gobooking.dto.company.CompanyDto;
import org.example.gobooking.dto.company.SaveAddressRequest;
import org.example.gobooking.dto.company.SaveCompanyRequest;
import org.example.gobooking.dto.request.SaveRoleChangeRequest;
import org.example.gobooking.dto.user.UserDto;
import org.example.gobooking.security.CurrentUser;
import org.example.gobooking.service.CompanyService;
import org.example.gobooking.service.CountryService;
import org.example.gobooking.service.DirectorService;
import org.example.gobooking.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/director")
@RequiredArgsConstructor
public class DirectorController {

    private final CompanyService companyService;

    private final CountryService countryService;

    private final UserService userService;

    private final DirectorService directorService;


    @GetMapping
    public String getDirectorPage(@AuthenticationPrincipal CurrentUser currentUser, ModelMap modelMap) {
        CompanyDto company = companyService.getCompanyByDirector(currentUser.getUser());
        modelMap.addAttribute("company", company);
        return "/director/director";
    }

    @GetMapping("/create-company")
    public String createCompanyPage(ModelMap modelMap) {
        modelMap.put("countries", countryService.getAllCountries());
        return "/director/addCompany";
    }

    @PostMapping("/create-company")
    public String createCompany(@Valid @ModelAttribute SaveCompanyRequest companyRequest, @Valid @ModelAttribute SaveAddressRequest saveAddressRequest) {
        companyService.save(companyRequest, saveAddressRequest);
        return "redirect:/director";
    }

    @GetMapping("/delete-company")
    public String deleteCompany(@RequestParam("id") int id) {
        System.out.println(id);
        companyService.deleteCompany(id);
        return "redirect:/director";
    }

    @GetMapping("/role-change-request-dashboard")
    public String promotionRequestDashboard(ModelMap modelMap,
                                            @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                            @RequestParam(value = "keyword", defaultValue = "") String keyword,
                                            @AuthenticationPrincipal CurrentUser currentUser) {
        CompanyDto company = companyService.getCompanyByDirector(currentUser.getUser());
        if (company != null) {
            PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
            Page<UserDto> userDtoList;
            if (keyword != null && !keyword.isEmpty()) {
                userDtoList = userService.getAllUsersByEmail(pageRequest, keyword);
            } else {
                userDtoList = userService.getAllUsers(pageRequest);
            }
            int totalPages = userDtoList.getTotalPages();
            if (totalPages > 0) {
                List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                        .boxed()
                        .toList();
                modelMap.addAttribute("pageNumbers", pageNumbers);
            }
            modelMap.addAttribute("companyId", company.getId());
            modelMap.addAttribute("userDtoList", userDtoList);
            return "/director/role_change_request_dashboard";
        }
        throw new CompanyNoCreateException("Company not found");
    }

    @PostMapping("/send-role-change-request")
    public String endRoleChangeRequest(@ModelAttribute SaveRoleChangeRequest roleChangeRequest) {
        directorService.sendWorkRequest(roleChangeRequest);
        return "redirect:/director/role-change-request-dashboard";
    }

}
