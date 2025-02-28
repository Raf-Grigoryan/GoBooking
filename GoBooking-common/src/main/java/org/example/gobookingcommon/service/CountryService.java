package org.example.gobookingcommon.service;



import org.example.gobookingcommon.entity.company.Country;

import java.util.List;

public interface CountryService {
    List<Country> getAllCountries();
    Country getCountryById(int id);
}
