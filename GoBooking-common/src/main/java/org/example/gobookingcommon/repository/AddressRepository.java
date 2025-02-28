package org.example.gobookingcommon.repository;


import org.example.gobookingcommon.entity.company.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Integer> {

    boolean existsByStreetAndApartmentNumber(String street, String apartmentNumber);

    Address getById(int id);
}
