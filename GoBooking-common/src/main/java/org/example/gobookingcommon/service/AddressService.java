package org.example.gobookingcommon.service;


import org.example.gobookingcommon.dto.company.SaveAddressRequest;
import org.example.gobookingcommon.entity.company.Address;

public interface AddressService {

    void saveAddress(Address address);

    boolean getAddressByStreetAndApartmentNumber(String street, String apartmentNumber);

    Address editAddress(SaveAddressRequest addressRequest, int id);
}
