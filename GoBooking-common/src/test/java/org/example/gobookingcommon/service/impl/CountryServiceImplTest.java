package org.example.gobookingcommon.service.impl;

import org.example.gobookingcommon.customException.CannotVerifyUserException;
import org.example.gobookingcommon.entity.company.Country;
import org.example.gobookingcommon.repository.CountryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CountryServiceTest {

    @Mock
    private CountryRepository countryRepository;

    @InjectMocks
    private CountryServiceImpl countryService;

    private Country country;

    @BeforeEach
    void setUp() {
        country = new Country(1, "Country1");
        MockitoAnnotations.openMocks(this);

    }

    @Test
    void testGetAllCountries_ShouldReturnCountryList() {
        Country country1 = new Country(1, "Country1");
        Country country2 = new Country(2, "Country2");
        List<Country> countries = Arrays.asList(country1, country2);

        when(countryRepository.findAll()).thenReturn(countries);

        List<Country> result = countryService.getAllCountries();

        assertNotNull(result, "Result should not be null");
        assertEquals(2, result.size(), "List should contain 2 countries");
        assertTrue(result.contains(country1), "List should contain Country1");
        assertTrue(result.contains(country2), "List should contain Country2");

        verify(countryRepository, times(1)).findAll(); // Ensure repository method is called once
    }

    @Test
    void testGetAllCountries_ShouldReturnEmptyList_WhenNoCountries() {
        List<Country> emptyList = List.of();
        when(countryRepository.findAll()).thenReturn(emptyList);

        List<Country> result = countryService.getAllCountries();

        assertNotNull(result, "Result should not be null");
        assertTrue(result.isEmpty(), "List should be empty");

        verify(countryRepository, times(1)).findAll(); // Ensure repository method is called once
    }

    @Test
    void testGetAllCountries_ShouldHandleRepositoryException() {
        when(countryRepository.findAll()).thenThrow(new RuntimeException("Database Error"));

        Exception exception = assertThrows(RuntimeException.class, () -> countryService.getAllCountries());

        assertEquals("Database Error", exception.getMessage(), "Exception message should match");

        verify(countryRepository, times(1)).findAll(); // Ensure repository method is called once
    }

    @Test
    void testGetCountryById_ShouldReturnCountry_WhenCountryExists() {
        when(countryRepository.findById(1)).thenReturn(Optional.of(country));

        Country result = countryService.getCountryById(1);

        assertNotNull(result, "Country should not be null");
        assertEquals(country.getId(), result.getId(), "Country ID should match");
        assertEquals(country.getName(), result.getName(), "Country name should match");

        verify(countryRepository, times(1)).findById(1); // Ensure findById is called once
    }

    @Test
    void testGetCountryById_ShouldThrowCannotVerifyUserException_WhenCountryNotFound() {
        when(countryRepository.findById(999)).thenReturn(Optional.empty());

        CannotVerifyUserException exception = assertThrows(CannotVerifyUserException.class, () -> countryService.getCountryById(999));

        assertEquals("Error: Cannot find user with id 999", exception.getMessage(), "Exception message should match");

        verify(countryRepository, times(1)).findById(999); // Ensure findById is called once
    }

    @Test
    void testGetCountryById_ShouldHandleRepositoryException() {
        when(countryRepository.findById(1)).thenThrow(new RuntimeException("Database Error"));

        Exception exception = assertThrows(RuntimeException.class, () -> countryService.getCountryById(1));

        assertEquals("Database Error", exception.getMessage(), "Exception message should match");

        verify(countryRepository, times(1)).findById(1); // Ensure findById is called once
    }
}