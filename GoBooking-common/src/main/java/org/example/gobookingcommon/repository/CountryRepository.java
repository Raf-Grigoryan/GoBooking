package org.example.gobookingcommon.repository;

import org.example.gobookingcommon.entity.company.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country, Integer> {

    Country getCountryById(int id);

}