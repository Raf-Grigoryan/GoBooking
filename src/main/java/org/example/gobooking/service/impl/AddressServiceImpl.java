package org.example.gobooking.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.gobooking.entity.company.Address;
import org.example.gobooking.repository.AddressRepository;
import org.example.gobooking.service.AddressService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    @Override
    public void saveAddress(Address address) {
        addressRepository.save(address);
    }

    @Override
    public boolean getAddressByStreetAndApartmentNumber(String street, String apartmentNumber) {
        return addressRepository.existsByStreetAndApartmentNumber(street,apartmentNumber);
    }



}
