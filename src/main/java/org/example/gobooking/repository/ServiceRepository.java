package org.example.gobooking.repository;

import org.example.gobooking.entity.work.Service;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceRepository extends JpaRepository<Service, Integer> {

    List<Service> findAllByWorker_id(int workerId);
}
