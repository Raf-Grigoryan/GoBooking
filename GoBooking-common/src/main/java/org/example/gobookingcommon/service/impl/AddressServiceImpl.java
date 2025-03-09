package org.example.gobookingcommon.service.impl;

import lombok.RequiredArgsConstructor;

import org.example.gobookingcommon.dto.company.SaveAddressRequest;
import org.example.gobookingcommon.entity.company.Address;
import org.example.gobookingcommon.repository.AddressRepository;
import org.example.gobookingcommon.repository.CountryRepository;
import org.example.gobookingcommon.service.AddressService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    private final CountryRepository countryRepository;

    @Override
    public void saveAddress(Address address) {
        if (address == null) {
            throw new IllegalArgumentException("Address cannot be null");
        }
        addressRepository.save(address);
    }

    @Override
    public boolean getAddressByStreetAndApartmentNumber(String street, String apartmentNumber) {
        return addressRepository.existsByStreetAndApartmentNumber(street, apartmentNumber);
    }

    @Override
    public Address editAddress(SaveAddressRequest addressRequest, int id) {
        if (addressRequest == null ||
                addressRequest.getRegion() == null ||
                addressRequest.getCity() == null ||
                addressRequest.getStreet() == null ||
                addressRequest.getApartmentNumber() == null) {
            throw new IllegalArgumentException("Invalid address request");
        }

        Address address = addressRepository.getById(id);
        address.setCountry(countryRepository.getCountryById(addressRequest.getCountryId()));
        address.setRegion(addressRequest.getRegion());
        address.setCity(addressRequest.getCity());
        address.setStreet(addressRequest.getStreet());
        address.setApartmentNumber(addressRequest.getApartmentNumber());
        saveAddress(address);
        return address;
    }
}
