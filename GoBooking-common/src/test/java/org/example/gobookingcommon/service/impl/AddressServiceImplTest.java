package org.example.gobookingcommon.service.impl;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.example.gobookingcommon.dto.company.SaveAddressRequest;
import org.example.gobookingcommon.entity.company.Address;
import org.example.gobookingcommon.entity.company.Country;
import org.example.gobookingcommon.repository.AddressRepository;
import org.example.gobookingcommon.repository.CountryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AddressServiceTest {
    @Mock
    private AddressRepository addressRepository;

    @Mock
    private CountryRepository countryRepository;

    @InjectMocks
    private AddressServiceImpl addressService;

    private Address address;
    private SaveAddressRequest validRequest;
    private Address existingAddress;

    private String street;
    private String apartmentNumber;

    @BeforeEach
    void setUp() {
        Country country = new Country();
        country.setId(1);
        country.setName("SomeCountry");

        address = new Address();
        address.setId(1);
        address.setCountry(country);
        address.setRegion("OldRegion");
        address.setCity("OldCity");
        address.setStreet("OldStreet");
        address.setApartmentNumber("100");

        street = "OldStreet";
        apartmentNumber = "100";

        validRequest = new SaveAddressRequest();
        validRequest.setCountryId(1);
        validRequest.setRegion("SomeRegion");
        validRequest.setCity("SomeCity");
        validRequest.setStreet("SomeStreet");
        validRequest.setApartmentNumber("101");

        existingAddress = new Address();
        existingAddress.setId(1);
        existingAddress.setCountry(new Country());
        existingAddress.setRegion("OldRegion");
        existingAddress.setCity("OldCity");
        existingAddress.setStreet("OldStreet");
        existingAddress.setApartmentNumber("100");
    }

    @Test
    void testEditAddress_Success() {
        // Arrange
        Country country = new Country();
        country.setId(1);
        country.setName("SomeCountry");

        when(addressRepository.getById(1)).thenReturn(existingAddress);
        when(countryRepository.getCountryById(validRequest.getCountryId())).thenReturn(country);

        Address updatedAddress = addressService.editAddress(validRequest, 1);

        assertNotNull(updatedAddress);
        assertEquals("SomeRegion", updatedAddress.getRegion());
        assertEquals("SomeCity", updatedAddress.getCity());
        assertEquals("SomeStreet", updatedAddress.getStreet());
        assertEquals("101", updatedAddress.getApartmentNumber());
        assertEquals(country, updatedAddress.getCountry());

        verify(addressRepository, times(1)).getById(1);
        verify(countryRepository, times(1)).getCountryById(validRequest.getCountryId());
    }

    @Test
    void testEditAddress_AddressNotFound() {
        when(addressRepository.getById(1)).thenThrow(new RuntimeException("Address not found"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> addressService.editAddress(validRequest, 1));

        assertEquals("Address not found", exception.getMessage());
    }

    @Test
    void testEditAddress_CountryNotFound() {
        when(addressRepository.getById(1)).thenReturn(existingAddress);
        when(countryRepository.getCountryById(validRequest.getCountryId())).thenThrow(new RuntimeException("Country not found"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> addressService.editAddress(validRequest, 1));

        assertEquals("Country not found", exception.getMessage());
    }

    @Test
    void testEditAddress_InvalidRequest() {
        SaveAddressRequest invalidRequest = new SaveAddressRequest();  // This is missing required fields like countryId

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> addressService.editAddress(invalidRequest, 1));

        assertEquals("Invalid address request", exception.getMessage());

        verify(addressRepository, times(0)).getById(anyInt());
    }


    @Test
    void testSaveAddress() {
        addressService.saveAddress(address);
        verify(addressRepository, times(1)).save(address);
    }

    @Test
    void testSaveAddress_NullAddress() {
        assertThrows(IllegalArgumentException.class, () -> addressService.saveAddress(null));
    }

    @Test
    void testSaveAddress_ThrowsException() {
        doThrow(new RuntimeException("Database error")).when(addressRepository).save(any(Address.class));
        assertThrows(RuntimeException.class, () -> addressService.saveAddress(address));
    }

    @Test
    void testGetAddressByStreetAndApartmentNumber_Found() {
        when(addressRepository.existsByStreetAndApartmentNumber(street, apartmentNumber)).thenReturn(true);

        boolean result = addressService.getAddressByStreetAndApartmentNumber(street, apartmentNumber);

        assertTrue(result);
        verify(addressRepository, times(1)).existsByStreetAndApartmentNumber(street, apartmentNumber);
    }

    @Test
    void testGetAddressByStreetAndApartmentNumber_NotFound() {
        when(addressRepository.existsByStreetAndApartmentNumber(street, apartmentNumber)).thenReturn(false);

        boolean result = addressService.getAddressByStreetAndApartmentNumber(street, apartmentNumber);

        assertFalse(result);
        verify(addressRepository, times(1)).existsByStreetAndApartmentNumber(street, apartmentNumber);
    }
}
