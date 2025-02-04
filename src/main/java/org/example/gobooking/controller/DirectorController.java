package org.example.gobooking.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.gobooking.dto.company.CompanyDto;
import org.example.gobooking.dto.company.SaveAddressRequest;
import org.example.gobooking.dto.company.SaveCompanyRequest;
import org.example.gobooking.security.CurrentUser;
import org.example.gobooking.service.CompanyService;
import org.example.gobooking.service.CountryService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/director")
@RequiredArgsConstructor
public class DirectorController {

    private final CompanyService companyService;

    private final CountryService countryService;


    @GetMapping
    public String getDirectorPage(@AuthenticationPrincipal CurrentUser currentUser, ModelMap modelMap) {
        CompanyDto company = companyService.getCompanyByDirector(currentUser.getUser());
        modelMap.addAttribute("company", company);
        return "/director/director";
    }

    @GetMapping("/create-company")
    public String createCompanyPage(ModelMap modelMap) {
        modelMap.put("countries",countryService.getAllCountries());
        return "/director/addCompany";
    }

    @PostMapping("/create-company")
    public String createCompany(@Valid @ModelAttribute SaveCompanyRequest companyRequest, @Valid @ModelAttribute SaveAddressRequest saveAddressRequest) {
        companyService.save(companyRequest,saveAddressRequest);
        return "redirect:/director";
    }

    @GetMapping("/delete-company")
    public String deleteCompany(@RequestParam("id") int id){
        System.out.println(id);
        companyService.deleteCompany(id);
        return "redirect:/director";
    }


}
