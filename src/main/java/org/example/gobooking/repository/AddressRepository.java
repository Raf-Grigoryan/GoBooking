package org.example.gobooking.repository;


import org.example.gobooking.entity.company.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Integer> {

    boolean existsByStreetAndApartmentNumber(String street, String apartmentNumber);
}
