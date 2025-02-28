package org.example.gobookingcommon.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.gobookingcommon.entity.projectFinance.ProjectFinance;
import org.example.gobookingcommon.repository.ProjectFinanceRepository;
import org.example.gobookingcommon.service.ProjectFinanceService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectFinanceServiceImpl implements ProjectFinanceService {

    private final static int BALANCE_ID = 1;

    private final ProjectFinanceRepository  projectFinanceRepository;



    @Override
    public synchronized void addFunds(BigDecimal money) {
        Optional<ProjectFinance> projectFinanceOpt = projectFinanceRepository.findById(BALANCE_ID);
        if (projectFinanceOpt.isEmpty()) {
            ProjectFinance projectFinance = new ProjectFinance();
            projectFinance.setBalance(money);
            projectFinanceRepository.save(projectFinance);
        }else {
            projectFinanceOpt.get().sum(money);
            projectFinanceRepository.save(projectFinanceOpt.get());
        }
    }

    @Override
    public double getProjectFinance() {
        Optional<ProjectFinance> projectFinanceOpt = projectFinanceRepository.findById(BALANCE_ID);
        return projectFinanceOpt.map(projectFinance -> projectFinance.getBalance().doubleValue()).orElse(0.0);
    }
}
