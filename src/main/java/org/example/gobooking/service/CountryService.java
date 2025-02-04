package org.example.gobooking.service;

import org.example.gobooking.entity.company.Country;

import java.util.List;

public interface CountryService {
    List<Country> getAllCountries();
    Country getCountryById(int id);
}
