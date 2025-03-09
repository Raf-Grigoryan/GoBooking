package org.example.gobookingcommon.service.impl;

import lombok.RequiredArgsConstructor;

import org.example.gobookingcommon.customException.CannotVerifyUserException;
import org.example.gobookingcommon.entity.company.Country;
import org.example.gobookingcommon.repository.CountryRepository;
import org.example.gobookingcommon.service.CountryService;
import org.mapstruct.Named;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;


    @Override
    public List<Country> getAllCountries() {
        return countryRepository.findAll();
    }

    @Override
    @Named("getCountryById")
    public Country getCountryById(int id) {
        Optional<Country> countryDb = countryRepository.findById(id);
        if (countryDb.isPresent()) {
            return countryDb.get();
        }
        throw new CannotVerifyUserException("Error: Cannot find user with id " + id);
    }
}
