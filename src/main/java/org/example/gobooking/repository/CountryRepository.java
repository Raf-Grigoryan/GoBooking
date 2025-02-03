package org.example.gobooking.repository;

import org.example.gobooking.entity.company.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country, Integer> {
}