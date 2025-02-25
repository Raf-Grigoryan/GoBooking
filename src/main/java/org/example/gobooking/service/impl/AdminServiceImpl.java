package org.example.gobooking.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.gobooking.entity.company.Company;
import org.example.gobooking.entity.user.Role;
import org.example.gobooking.entity.user.User;
import org.example.gobooking.repository.CompanyRepository;
import org.example.gobooking.repository.UserRepository;
import org.example.gobooking.service.AdminService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final CompanyRepository companyRepository;

    private final UserRepository userRepository;

    @Transactional
    @Override
    public void deleteCompany(int directorId) {
        Optional<User> userOpt = userRepository.findById(directorId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            Company company = user.getCompany();
            if (companyRepository.existsCompanyByDirector(user)) {
                List<User> workers = userRepository.findUserByCompany_Id(company.getId());
                user.setCompany(null);
                user.setRole(Role.USER);
                for (User worker : workers) {
                    worker.setCompany(null);
                    worker.setRole(Role.USER);
                    userRepository.save(worker);
                }
                userRepository.save(user);
                companyRepository.delete(company);
            }
        }

    }
}
