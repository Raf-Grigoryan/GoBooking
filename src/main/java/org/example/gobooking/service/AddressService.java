package org.example.gobooking.service;

import org.example.gobooking.entity.company.Address;


public interface AddressService {

    void saveAddress(Address address);

    boolean getAddressByStreetAndApartmentNumber(String street, String apartmentNumber);



}
