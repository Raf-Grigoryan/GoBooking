package org.example.gobookingcommon.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.example.gobookingcommon.entity.company.Company;
import org.example.gobookingcommon.entity.user.Role;
import org.example.gobookingcommon.entity.user.User;
import org.example.gobookingcommon.repository.CompanyRepository;
import org.example.gobookingcommon.repository.UserRepository;
import org.example.gobookingcommon.service.AdminService;
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
