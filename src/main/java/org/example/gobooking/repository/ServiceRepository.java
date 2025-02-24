package org.example.gobooking.repository;

import org.example.gobooking.entity.work.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ServiceRepository extends JpaRepository<Service, Integer> {

    List<Service> findAllByWorker_id(int workerId);

    int countByWorker_Company_Director_Id(int companyId);

    @Query("SELECT s FROM Service s JOIN s.worker u WHERE u.company.id = :companyId ORDER BY function('RAND') limit 5")
    List<Service> findRandomServicesByCompanyId(@Param("companyId") int companyId);

    @Query("SELECT s FROM Service s " +
            "JOIN s.worker u " +
            "WHERE u.company.director.id = :directorId " +
            "ORDER BY s.id DESC")
    List<Service> findAllServicesByDirectorIdDesc(@Param("directorId") int directorId);


}
