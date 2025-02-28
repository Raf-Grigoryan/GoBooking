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
        addressRepository.save(address);
    }

    @Override
    public boolean getAddressByStreetAndApartmentNumber(String street, String apartmentNumber) {
        return addressRepository.existsByStreetAndApartmentNumber(street, apartmentNumber);
    }

    @Override
    public Address editAddress(SaveAddressRequest addressRequest, int id) {
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
