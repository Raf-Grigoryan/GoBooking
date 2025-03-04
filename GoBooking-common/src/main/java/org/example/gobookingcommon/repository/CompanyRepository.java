package org.example.gobookingcommon.repository;



import org.example.gobookingcommon.entity.company.Company;
import org.example.gobookingcommon.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface CompanyRepository extends JpaRepository<Company, Integer> {

    Optional<Company> findCompanyByDirectorId(int directorId);

    Company findCompanyByDirector(User director);

    Page<Company> findCompaniesByNameContainingAndValid(String name, Pageable pageable, boolean valid);

    Page<Company> findCompaniesByValid(boolean valid, Pageable pageable);

    boolean existsCompanyByDirector(User director);

    void deleteByDirector(User director);

    int countCompaniesByValid(boolean valid);

    Company getCompanyById(int id);

}
